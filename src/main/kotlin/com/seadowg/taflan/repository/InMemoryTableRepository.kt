package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Table

class InMemoryTableRepository : TableRepository {

    private val tables = mutableListOf<Table>()

    override fun create(name: String) {
        tables.add(Table(name))
    }

    override fun fetchAll(): List<Table> {
        return tables.toList()
    }

}
