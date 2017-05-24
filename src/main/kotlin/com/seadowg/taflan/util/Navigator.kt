package com.seadowg.taflan.util

import android.app.Activity
import android.content.Intent
import com.seadowg.taflan.activity.EditItemActivity
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class Navigator(val activity: Activity) {
    fun editItem(table: Table.Existing, item: Item.Existing) {
        val intent = Intent(activity, EditItemActivity::class.java)
        intent.putExtra(EditItemActivity.EXTRA_TABLE, table)
        intent.putExtra(EditItemActivity.EXTRA_ITEM, item)

        activity.startActivity(intent)
    }
}