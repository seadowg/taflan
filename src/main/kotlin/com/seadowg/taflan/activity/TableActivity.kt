package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.github.clans.fab.FloatingActionMenu
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.ItemItem
import com.seadowg.taflan.view.colorDrawable

class TableActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table)

        val table = intent.extras.getSerializable(EXTRA_TABLE) as Table
        setupToolbar(table.name, color = table.colorDrawable(this))

        findViewById(R.id.add_item).reactive().clicks.bind(this) { _, _ ->
            val fabMenu = findViewById(R.id.fab) as FloatingActionMenu
            fabMenu.close(true)

            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(NewItemActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        val itemsList = findViewById(R.id.items) as ViewGroup
        itemsList.removeAllViews()

        val table = intent.extras.getSerializable(EXTRA_TABLE) as Table
        tableRepository.fetch(table.id).items.forEach {
            val itemItem = ItemItem.inflate(it, itemsList, this)
            itemsList.addView(itemItem)
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}