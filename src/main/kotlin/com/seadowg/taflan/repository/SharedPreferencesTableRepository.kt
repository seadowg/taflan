package com.seadowg.taflan.repository

import android.content.SharedPreferences
import com.google.gson.Gson
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class SharedPreferencesTableRepository(val sharedPreferences: SharedPreferences) : TableRepository {

    override fun create(table: Table.New): Table.Existing {
        val createdTable = Table.Existing("1", table.name, table.color, table.fields, table.items)
        
        val json = Gson().toJson(createdTable)
        sharedPreferences.edit().putString("tables:${createdTable.id}", json).apply()
        
        return createdTable
    }

    override fun fetch(id: String): Table.Existing {
        val json = sharedPreferences.getString("tables:$id", null)
        return Gson().fromJson(json, Table.Existing::class.java)
    }

    override fun fetchAll(): List<Table.Existing> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addItem(table: Table.Existing, item: Item.New) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addField(table: Table.Existing, field: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateItem(table: Table.Existing, item: Item.Existing) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteItem(table: Table.Existing, item: Item.Existing) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}