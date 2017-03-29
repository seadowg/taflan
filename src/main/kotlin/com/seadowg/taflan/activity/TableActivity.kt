package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
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
        setupFabHelper()
    }

    override fun onResume() {
        super.onResume()

        val intentTable = intent.extras.getSerializable(EXTRA_TABLE) as Table
        val table = tableRepository.fetch(intentTable.id)

        renderItems(table)
        setupFAB(table)
    }

    private fun setupFAB(table: Table) {
        val fabMenu = findViewById(R.id.fab) as FloatingActionMenu

        findViewById(R.id.add_item).reactive().clicks.bind(this) { _, _ ->
            fabMenu.close(true)

            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(NewItemActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }

        findViewById(R.id.add_field).reactive().clicks.bind(this) { _, _ ->
            fabMenu.close(true)

            val intent = Intent(this, NewFieldActivity::class.java)
            intent.putExtra(NewFieldActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }
    }

    private fun renderItems(table: Table) {
        val itemsList = findViewById(R.id.items) as ViewGroup
        itemsList.removeAllViews()

        table.items.forEach {
            val itemItem = ItemItem.inflate(it, table, itemsList, this)
            itemsList.addView(itemItem)
        }
    }

    private fun setupFabHelper() {
        if (TEST_MODE) {
            val fabHelper = findViewById(R.id.fab_helper)
            fabHelper.visibility = View.VISIBLE

            fabHelper.reactive().clicks.bind(this) { _, _ ->
                val fabMenu = findViewById(R.id.fab) as FloatingActionMenu
                fabMenu.open(true)
            }
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}