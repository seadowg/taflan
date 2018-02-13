package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import android.support.v7.widget.DividerItemDecoration
import com.seadowg.taflan.repository.ReactiveTableRepository


class TableActivity : TaflanActivity(), Reference {

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

        setupFabHelper()

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

    private fun setupFAB(table: Table) {
        val fabMenu = findViewById<FloatingActionMenu>(R.id.fab)

        findViewById<View>(R.id.add_item).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val intent = Intent(this, NewItemActivity::class.java)
            intent.putExtra(NewItemActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }

        findViewById<View>(R.id.add_field).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val intent = Intent(this, NewFieldActivity::class.java)
            intent.putExtra(NewFieldActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }

        findViewById<View>(R.id.export).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val csv = CSV(table.fields, table.items.map { it.values })

            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/csv"
            shareIntent.putExtra(Intent.EXTRA_TEXT, csv.toString())
            startActivity(Intent.createChooser(shareIntent, "Export \"${table.name}\" as .csv"))
        }

        findViewById<View>(R.id.fields).reactive().clicks.bind(this) {
            fabMenu.close(true)

            val intent = Intent(this, FieldsActivity::class.java)
            intent.putExtra(NewFieldActivity.EXTRA_TABLE, table)

            startActivity(intent)
        }
    }

    private fun setupFabHelper() {
        if (TEST_MODE) {
            val fabHelper = findViewById<View>(R.id.fab_helper)
            fabHelper.visibility = View.VISIBLE

            fabHelper.reactive().clicks.bind(this) {
                val fabMenu = findViewById<FloatingActionMenu>(R.id.fab) as FloatingActionMenu
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