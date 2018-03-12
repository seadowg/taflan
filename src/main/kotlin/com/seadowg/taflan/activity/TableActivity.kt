package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import com.github.clans.fab.FloatingActionMenu
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.adapter.ItemAdapter
import com.seadowg.taflan.csv.CSV
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.colorDrawable


class TableActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()
    private val tableID: String by lazy {
        (intent.extras.getSerializable(EXTRA_TABLE) as Table.Existing).id
    }

    private val table by lazy { tableRepository.fetch(tableID) }

    private val navigator = Navigator(this)

    private val itemAdapter: ItemAdapter by lazy {
        ItemAdapter(this, tableRepository.store, tableID, navigator)
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

            itemAdapter.notifyDataSetChanged()
            setupFAB(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.table_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.edit -> {
                val intent = Intent(this, EditTableActivity::class.java)
                intent.putExtra(EditTableActivity.EXTRA_TABLE, table.currentValue)

                startActivity(intent)
                true
            }

            R.id.export -> {
                exportTable()
                true
            }

            R.id.add_field -> {
                val intent = Intent(this, NewFieldActivity::class.java)
                intent.putExtra(NewFieldActivity.EXTRA_TABLE, table.currentValue)

                startActivity(intent)
                true
            }

            else -> false
        }
    }

    private fun exportTable() {
        val table = table.currentValue
        val csv = CSV(table.fields, table.items.map { it.values })

        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/csv"
        shareIntent.putExtra(Intent.EXTRA_TEXT, csv.toString())
        startActivity(Intent.createChooser(shareIntent, "Export \"${table.name}\" as .csv"))
    }

    private fun setupFAB(table: Table) {
        findViewById<FloatingActionMenu>(R.id.fab).reactive().clicks.bind(this) {
            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(NewItemActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }
    }

    companion object {
        private val EXTRA_TABLE = "extra_table"

        fun intent(context: Context, table: Table.Existing): Intent {
            return Intent(context, TableActivity::class.java)
                    .putExtra(EXTRA_TABLE, table)
        }
    }
}