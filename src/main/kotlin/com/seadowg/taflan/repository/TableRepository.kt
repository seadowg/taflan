package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

interface TableRepository {
    fun create(table: Table.New): Table.Existing
    fun fetchAll(): List<Table.Existing>
    fun clear()
    fun addItem(table: Table.Existing, item: Item.New): Table.Existing
    fun fetch(id: String): Table.Existing
    fun addField(table: Table.Existing, field: String): Table.Existing
    fun updateItem(table: Table.Existing, item: Item.Existing): Table.Existing
    fun deleteItem(table: Table.Existing, item: Item.Existing): Table.Existing
}