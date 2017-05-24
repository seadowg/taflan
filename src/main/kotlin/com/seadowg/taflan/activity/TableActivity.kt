package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ListView
import com.github.clans.fab.FloatingActionMenu
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.adapter.ItemAdapter
import com.seadowg.taflan.R
import com.seadowg.taflan.csv.CSV
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.util.Reference
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.colorDrawable

class TableActivity : TaflanActivity(), Reference {

    private val tableRepository: TableRepository by injector.instance()
    private val navigator = Navigator(this)

    private val tableID: String by lazy {
        (intent.extras.getSerializable(EXTRA_TABLE) as Table.Existing).id
    }

    private val itemAdapter: ItemAdapter by lazy {
        ItemAdapter(this, tableRepository, tableID, navigator)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.table)

        val table = tableRepository.fetch(tableID)
        setupToolbar(table.name, color = table.colorDrawable(this), backArrow = true)
        setupFabHelper()

        val itemsList = findViewById(R.id.items) as ListView
        itemsList.adapter = itemAdapter
    }

    override fun onResume() {
        super.onResume()
        reloadData()
    }

    private fun reloadData() {
        val intentTable = intent.extras.getSerializable(EXTRA_TABLE) as Table.Existing
        val table = tableRepository.fetch(intentTable.id)

        itemAdapter.notifyDataSetChanged()
        setupFAB(table)
    }

    private fun setupFAB(table: Table) {
        val fabMenu = findViewById(R.id.fab) as FloatingActionMenu

        findViewById(R.id.add_item).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(NewItemActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }

        findViewById(R.id.add_field).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val intent = Intent(this, NewFieldActivity::class.java)
            intent.putExtra(NewFieldActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }

        findViewById(R.id.export).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val csv = CSV(table.fields, table.items.map { it.values })

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/csv"
            shareIntent.putExtra(Intent.EXTRA_TEXT, csv.toString())
            startActivity(Intent.createChooser(shareIntent, "Export \"${table.name}\" as .csv"))
        }
    }

    private fun setupFabHelper() {
        if (TEST_MODE) {
            val fabHelper = findViewById(R.id.fab_helper)
            fabHelper.visibility = View.VISIBLE

            fabHelper.reactive().clicks.bind(this) {
                val fabMenu = findViewById(R.id.fab) as FloatingActionMenu
                fabMenu.open(true)
            }
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