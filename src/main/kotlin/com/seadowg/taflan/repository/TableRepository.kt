package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.util.ReactiveStore
import com.seadowg.taflan.util.Store

interface TableRepository : Store<Table.Existing> {
    fun create(table: Table.New): Table.Existing
    override fun fetchAll(): List<Table.Existing>
    fun clear()
    fun addItem(table: Table.Existing, item: Item.New): Table.Existing
    override fun fetch(id: String): Table.Existing
    fun addField(table: Table.Existing, field: String): Table.Existing
    fun updateItem(table: Table.Existing, item: Item.Existing): Table.Existing
    fun deleteItem(table: Table.Existing, item: Item.Existing): Table.Existing
}

class ReactiveTableRepository(tableRepository: TableRepository) : ReactiveStore<Table.Existing, TableRepository>(tableRepository)