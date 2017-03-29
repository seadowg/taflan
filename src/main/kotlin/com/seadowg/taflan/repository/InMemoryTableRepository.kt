package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table

class InMemoryTableRepository : TableRepository {

    private val tables = mutableListOf<Table>()

    override fun create(name: String, color: Color) {
        tables.add(Table(name, color))
    }

    override fun fetchAll(): List<Table> {
        return tables.toList()
    }

}
