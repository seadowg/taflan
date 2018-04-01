package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.seadowg.taflan.R
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.tracking.Tracker
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.TableItem

class TablesActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()
    private val tracker: Tracker by injector.instance()

    private val tables by lazy { tableRepository.fetchAll() }

    private val fab by lazy { findViewById<View>(R.id.fab).reactive() }
    private val tablesList by lazy { findViewById<ViewGroup>(R.id.tables) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch)
        setupToolbar(getString(R.string.app_title))

        fab.clicks.bind(this) {
            startActivity(Intent(this, NewTableActivity::class.java))
        }

        tables.bind(this) {
            tracker.track("load_tables", value = it.size.toLong())

            tablesList.removeAllViews()

            it.forEach { table ->
                val tableItem = TableItem.inflate(table, tablesList, this)

                tableItem.reactive().clicks.bind(this) {
                    tracker.track("load_items", value = table.items.size.toLong())

                    val intent = TableActivity.intent(this, table)
                    startActivity(intent)
                }

                tablesList.addView(tableItem)
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
