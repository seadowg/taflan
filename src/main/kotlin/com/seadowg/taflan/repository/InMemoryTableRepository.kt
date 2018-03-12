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

    override fun fetch(id: String): Table.Existing {
        return tables.single { it.id == id }
    }

    override fun fetchAll(): List<Table.Existing> {
        return tables.toList()
    }

    override fun addItem(table: Table.Existing, item: Item.New): Table.Existing {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val savedItem = Item.Existing(generateID(), item.values)

        val updatedTable = Table.Existing(table.id, table.name, table.color, fields = table.fields, items = table.items + savedItem)
        tables.add(updatedTable)
        return updatedTable
    }

    override fun addField(table: Table.Existing, field: String): Table.Existing {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val migratedItems = table.items.map { Item.Existing(it.id, it.values + "") }
        val updatedTable = Table.Existing(table.id, table.name, table.color, fields = table.fields + field, items = migratedItems)
        tables.add(updatedTable)
        return updatedTable
    }

    override fun updateItem(table: Table.Existing, item: Item.Existing): Table.Existing {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val updatedItems = table.items.map { oldItem ->
            if (oldItem.id == item.id) {
                item
            } else {
                oldItem
            }
        }

        val updatedTable = Table.Existing(table.id, table.name, table.color, fields = table.fields, items = updatedItems)
        tables.add(updatedTable)
        return updatedTable
    }

    override fun deleteItem(table: Table.Existing, item: Item.Existing): Table.Existing {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        val updatedItems = table.items - item
        val updatedTable = Table.Existing(table.id, table.name, table.color, fields = table.fields, items = updatedItems)
        tables.add(updatedTable)
        return table
    }

    override fun clear() {
        tables.clear()
    }

    private fun generateID(): String {
        return idCounter++.toString()
    }
}
