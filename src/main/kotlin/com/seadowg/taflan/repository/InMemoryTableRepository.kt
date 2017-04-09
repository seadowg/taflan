package com.seadowg.taflan.repository

import com.seadowg.taflan.R.id.items
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class InMemoryTableRepository : TableRepository {

    private val tables = mutableListOf<Table>()
    private var idCounter = 0

    override fun create(name: String, color: Color) {
        tables.add(Table(name, color, fields = listOf("Name")))
    }

    override fun addItem(table: Table, item: Item.New) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val savedItem = Item.Existing(generateID(), item.values)

        tables.add(Table(table.name, table.color, fields = table.fields, items = table.items + savedItem))
    }

    override fun updateItem(table: Table, item: Item.Existing) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val updatedItems = table.items.map { oldItem ->
            if (oldItem.id == item.id) {
                item
            } else {
                oldItem
            }
        }

        tables.add(Table(table.name, table.color, fields = table.fields, items = updatedItems))
    }

    override fun addField(table: Table, field: String) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        tables.add(Table(table.name, table.color, fields = table.fields + field, items = table.items))
    }

    override fun fetch(id: String): Table {
        return tables.single { it.id == id }
    }

    override fun fetchAll(): List<Table> {
        return tables.toList()
    }

    override fun clear() {
        tables.clear()
    }

    private fun generateID(): String {
        return idCounter++.toString()
    }

}
