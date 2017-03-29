package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

interface TableRepository {
    fun create(name: String, color: Color)
    fun fetchAll(): List<Table>
    fun clear()
    fun addItem(table: Table, item: Item)
    fun fetch(id: String): Table
    fun addField(table: Table, field: String)
}