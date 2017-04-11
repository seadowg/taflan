package com.seadowg.taflan.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import java.util.*

class SharedPreferencesTableRepository(val sharedPreferences: SharedPreferences) : TableRepository {

    private val gson = Gson()

    override fun create(table: Table.New): Table.Existing {
        val createdTable = Table.Existing(generateID(), table.name, table.color, table.fields, table.items)

        storeTable(createdTable)
        return createdTable
    }

    override fun fetch(id: String): Table.Existing {
        val json = sharedPreferences.getString("tables:$id", null)
        return gson.fromJson(json, Table.Existing::class.java)
    }

    override fun fetchAll(): List<Table.Existing> {
        val tableIDs = allTableKeys()
                .map { it.split(":")[1] }

        return tableIDs.map { fetch(it) }
    }

    override fun addItem(table: Table.Existing, item: Item.New): Table.Existing {
        val createdItem = Item.Existing(generateID(), values = item.values)
        val updatedTable = table.copy(items = table.items + createdItem)

        storeTable(updatedTable)
        return updatedTable
    }

    override fun addField(table: Table.Existing, field: String): Table.Existing {
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

    override fun deleteItem(table: Table.Existing, item: Item.Existing): Table.Existing {
        val updatedTable = table.copy(items = table.items - item)
        storeTable(updatedTable)
        return updatedTable
    }

    override fun clear() {
        allTableKeys().forEach { sharedPreferences.edit().remove(it).apply() }
    }

    private fun allTableKeys() = sharedPreferences.all
            .filter { it.key.startsWith("tables:") }.map { it.key }

    private fun storeTable(updatedTable: Table.Existing) {
        val json = gson.toJson(updatedTable)
        sharedPreferences.edit().putString("tables:${updatedTable.id}", json).apply()
    }

    private fun generateID() = UUID.randomUUID().toString()
}