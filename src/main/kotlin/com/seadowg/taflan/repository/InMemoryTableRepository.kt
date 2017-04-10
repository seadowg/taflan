package com.seadowg.taflan.repository

import com.seadowg.taflan.R.id.items
import com.seadowg.taflan.domain.Color
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

    override fun fetch(id: String): Table.Existing {
        return tables.single { it.id == id }
    }

    override fun fetchAll(): List<Table.Existing> {
        return tables.toList()
    }

    override fun addItem(table: Table.Existing, item: Item.New) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val savedItem = Item.Existing(generateID(), item.values)

        tables.add(Table.Existing(table.id, table.name, table.color, fields = table.fields, items = table.items + savedItem))
    }

    override fun addField(table: Table.Existing, field: String) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        tables.add(Table.Existing(table.id, table.name, table.color, fields = table.fields + field, items = table.items))
    }

    override fun updateItem(table: Table.Existing, item: Item.Existing) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val updatedItems = table.items.map { oldItem ->
            if (oldItem.id == item.id) {
                item
            } else {
                oldItem
            }
        }

        tables.add(Table.Existing(table.id, table.name, table.color, fields = table.fields, items = updatedItems))
    }

    override fun deleteItem(table: Table.Existing, item: Item.Existing) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val updatedItems = table.items - item
        tables.add(Table.Existing(table.id, table.name, table.color, fields = table.fields, items = updatedItems))
    }

    override fun clear() {
        tables.clear()
    }

    private fun generateID(): String {
        return idCounter++.toString()
    }
}
