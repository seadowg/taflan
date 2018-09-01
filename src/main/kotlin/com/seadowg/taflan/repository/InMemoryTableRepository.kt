package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class InMemoryTableRepository : TableRepository {
    private val tables = mutableListOf<Table.Existing>()

    private var idCounter = 0

    override fun create(table: Table.New): Table.Existing {
        val createdTable = Table.Existing(generateID(), table.name, table.color, table.fields, table.items)
        tables.add(createdTable)
        return createdTable
    }

    override fun save(table: Table.Existing) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun fetch(id: String): Table.Existing? {
        return tables.find { it.id == id }
    }

    override fun fetchAll(): List<Table.Existing> {
        return tables.toList()
    }

    override fun fetchItems(tableID: String): List<Item.Existing> {
        return tables.find { it.id == tableID }!!.items
    }

    override fun addItem(tableID: String, item: Item.New): Item.Existing {
        val tableToAddTo = tables.single { it.id == tableID }
        tables.remove(tableToAddTo)

        val savedItem = Item.Existing(generateID(), item.values)

        val updatedTable = Table.Existing(tableToAddTo.id, tableToAddTo.name, tableToAddTo.color, fields = tableToAddTo.fields, items = tableToAddTo.items + savedItem)
        tables.add(updatedTable)
        return savedItem
    }

    override fun addField(tableID: String, field: String): Table.Existing {
        val tableToAddTo = tables.single { it.id == tableID }
        tables.remove(tableToAddTo)

        val migratedItems = tableToAddTo.items.map { Item.Existing(it.id, it.values + "") }
        val updatedTable = Table.Existing(tableToAddTo.id, tableToAddTo.name, tableToAddTo.color, fields = tableToAddTo.fields + field, items = migratedItems)
        tables.add(updatedTable)
        return updatedTable
    }

    override fun updateItem(table: Table.Existing, item: Item.Existing): Table.Existing {
        val tableToUpdate = tables.single { it.id == table.id }
        tables.remove(tableToUpdate)

        val updatedItems = tableToUpdate.items.map { oldItem ->
            if (oldItem.id == item.id) {
                item
            } else {
                oldItem
            }
        }

        val updatedTable = Table.Existing(tableToUpdate.id, tableToUpdate.name, tableToUpdate.color, fields = tableToUpdate.fields, items = updatedItems)
        tables.add(updatedTable)
        return updatedTable
    }

    override fun deleteItem(tableID: String, item: Item.Existing): Table.Existing {
        val tableToUpdate = tables.single { it.id == tableID }
        tables.remove(tableToUpdate)

        val updatedItems = tableToUpdate.items - item
        val updatedTable = Table.Existing(tableToUpdate.id, tableToUpdate.name, tableToUpdate.color, fields = tableToUpdate.fields, items = updatedItems)
        tables.add(updatedTable)
        return tableToUpdate
    }

    override fun delete(table: Table.Existing) {
        tables.remove(table)
    }

    override fun clear() {
        tables.clear()
    }

    private fun generateID(): String {
        return idCounter++.toString()
    }
}
