package com.seadowg.taflan.util

import android.app.Activity
import android.content.Intent
import com.seadowg.taflan.activity.*
import com.seadowg.taflan.csv.CSV
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class Navigator(val activity: Activity) {

    fun newTable() {
        start(Intent(activity, NewTableActivity::class.java))
    }

    fun showTable(table: Table.Existing) {
        val intent = TableActivity.intent(activity, table)
        start(intent)
    }

    fun editTable(table: Table.Existing) {
        val intent = EditTableActivity.intent(activity, table)
        start(intent)
    }

    fun shareTable(tableName: String, csv: CSV) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/csv"
        shareIntent.putExtra(Intent.EXTRA_TEXT, csv.toString())
        start(Intent.createChooser(shareIntent, "Export \"$tableName\" as .csv"))
    }

    fun newItem(table: Table.Existing) {
        start(NewItemActivity.intent(activity, table))
    }

    fun editItem(table: Table.Existing, item: Item.Existing) {
        val intent = EditItemActivity.intent(activity, table, item)
        start(intent)
    }

    fun newField(table: Table.Existing) {
        start(NewFieldActivity.intent(activity, table))
    }

    fun returnToTables() {
        finish()
        start(Intent(activity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        })
    }

    private fun start(intent: Intent) {
        activity.startActivity(intent)
    }

    private fun finish() {
        activity.finish()
    }
}