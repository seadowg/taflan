package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class InMemoryTableRepository : TableRepository {
    private val tables = mutableListOf<Table>()

    override fun create(name: String, color: Color) {
        tables.add(Table(name, color))
    }

    override fun addItem(table: Table, item: Item) {
        val table = tables.single { it.id == table.id }
        tables.remove(table)

        tables.add(Table(table.name, table.color, items = table.items + listOf(item)))
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

}
