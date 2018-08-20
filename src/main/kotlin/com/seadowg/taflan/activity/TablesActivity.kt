package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.github.salomonbrys.kodein.instance
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.seadowg.taflan.R
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.tracking.Tracker
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.TableItem
import kotlinx.android.synthetic.main.launch.*

class TablesActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()
    private val tracker: Tracker by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch)
        setupToolbar(getString(R.string.app_title))

        tracker.track("load_tables", value = tableRepository.fetchAll().size.toLong())

        fab.setOnClickListener { navigator.newTable() }
    }

    override fun onResume() {
        super.onResume()

        tableRepository.fetchAll().let {
            tables.removeAllViews()

            it.forEach { table ->
                val tableItem = TableItem.inflate(table, tables, this)

                tableItem.reactive().clicks.bind(this) {
                    tracker.track("load_items", value = table.items.size.toLong())
                    navigator.showTable(table)
                }

                tables.addView(tableItem)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.open_source_licenses -> {
                startActivity(Intent(this, OssLicensesMenuActivity::class.java))
                true
            }

            R.id.settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }

            else -> false
        }
    }
}
