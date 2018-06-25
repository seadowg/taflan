package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.github.salomonbrys.kodein.instance
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.seadowg.taflan.R
import com.seadowg.taflan.adapter.ItemAdapter
import com.seadowg.taflan.csv.CSV
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.colorDrawable


class TableActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()

    private val tableID: String by lazy { intent.extras.getString(EXTRA_TABLE) }
    private val table by lazy { tableRepository.fetch(tableID)!! }

    private val itemAdapter: ItemAdapter by lazy {
        ItemAdapter(this, tableRepository, tableID, navigator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table)

        val itemsList = findViewById<RecyclerView>(R.id.items)

        val layoutManager = LinearLayoutManager(this)
        itemsList.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        dividerItemDecoration.setDrawable(getDrawable(R.drawable.list_divider))
        itemsList.addItemDecoration(dividerItemDecoration)

        itemsList.adapter = itemAdapter

        table.bind(this) {
            setupToolbar(it.name, color = it.colorDrawable(this), backArrow = true)
            setupFAB(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.table_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.edit -> {
                navigator.editTable(table.currentValue)
            }

            R.id.export -> {
                exportTable()
            }

            R.id.add_field -> {
                navigator.newField(table.currentValue)
            }

            else -> return false
        }

        return true
    }

    private fun exportTable() {
        val table = table.currentValue
        val tableName = table.name
        val csv = CSV(table.fields, table.items.map { it.values })

        navigator.shareTable(tableName, csv)
    }

    private fun setupFAB(table: Table.Existing) {
        findViewById<FloatingActionButton>(R.id.fab).reactive().clicks.bind(this) {
            navigator.newItem(table)
        }
    }

    companion object {
        private val EXTRA_TABLE = "extra_table"

        fun intent(context: Context, table: Table.Existing): Intent {
            return Intent(context, TableActivity::class.java)
                    .putExtra(EXTRA_TABLE, table.id)
        }
    }
}