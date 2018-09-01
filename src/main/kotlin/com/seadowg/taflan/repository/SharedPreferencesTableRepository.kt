package com.seadowg.taflan.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import java.util.*

class SharedPreferencesTableRepository(private val sharedPreferences: SharedPreferences) : TableRepository {
    private val gson = Gson()

    override fun create(table: Table.New): Table.Existing {
        val createdTable = Table.Existing(generateID(), table.name, table.color, table.fields, table.items)

        storeTable(createdTable)
        return createdTable
    }

    override fun save(table: Table.Existing) {
        storeTable(table)
    }

    override fun fetch(id: String): Table.Existing? {
        val json = sharedPreferences.getString("tables:$id", null)
        return json?.let {
            gson.fromJson(it, Table.Existing::class.java)
        }
    }

    override fun fetchAll(): List<Table.Existing> {
        val tableIDs = allTableKeys()
                .map { it.split(":")[1] }

        return tableIDs.mapNotNull { fetch(it) }
    }

    override fun fetchItems(tableID: String): List<Item.Existing> {
        return fetch(tableID)!!.items
    }

    override fun addItem(tableID: String, item: Item.New): Item.Existing {
        val createdItem = Item.Existing(generateID(), values = item.values)
        val table = fetch(tableID)!!
        val updatedTable = table.copy(items = table.items + createdItem)

        storeTable(updatedTable)
        return createdItem
    }

    override fun addField(tableID: String, field: String): Table.Existing {
        val table = fetch(tableID)!!
        val migratedItems = table.items.map { it.copy(values = it.values + "") }
        val updatedTable = table.copy(fields = table.fields + field, items = migratedItems)

        storeTable(updatedTable)
        return updatedTable
    }

    override fun updateItem(table: Table.Existing, item: Item.Existing): Table.Existing {
        val updatedItems = table.items.map {
            if (it.id == item.id) {
                item
            } else {
                it
            }
        }

        val updatedTable = table.copy(items = updatedItems)
        storeTable(updatedTable)
        return updatedTable
    }

    override fun deleteItem(tableID: String, item: Item.Existing): Table.Existing {
        val table = fetch(tableID)!!
        val updatedTable = table.copy(items = table.items - item)
        storeTable(updatedTable)
        return updatedTable
    }

    override fun delete(table: Table.Existing) {
        sharedPreferences.edit().remove(keyForTable(table)).apply()
    }

    override fun clear() {
        allTableKeys().forEach { sharedPreferences.edit().remove(it).apply() }
    }

    private fun allTableKeys() = sharedPreferences.all
            .filter { it.key.startsWith("tables:") }.map { it.key }

    private fun storeTable(updatedTable: Table.Existing) {
        val json = gson.toJson(updatedTable)
        sharedPreferences.edit().putString(keyForTable(updatedTable), json).apply()
    }

    private fun keyForTable(table: Table.Existing) = "tables:${table.id}"

    private fun generateID() = UUID.randomUUID().toString()
}