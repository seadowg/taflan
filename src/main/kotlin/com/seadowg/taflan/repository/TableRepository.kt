package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

interface TableRepository {
    fun create(table: Table.New): Table.Existing
    fun fetchAll(): List<Table.Existing>
    fun clear()
    fun addItem(tableID: String, item: Item.New): Item.Existing
    fun fetch(id: String): Table.Existing?
    fun addField(tableID: String, field: String): Table.Existing
    fun updateItem(table: Table.Existing, item: Item.Existing): Table.Existing
    fun deleteItem(tableID: String, item: Item.Existing): Table.Existing
    fun save(table: Table.Existing)
    fun delete(table: Table.Existing)
    fun fetchItems(tableID: String): List<Item.Existing>
}