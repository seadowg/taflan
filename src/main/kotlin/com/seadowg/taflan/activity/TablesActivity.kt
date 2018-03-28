package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.TableItem

class TablesActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()
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
            tablesList.removeAllViews()

            it.forEach { table ->
                val tableItem = TableItem.inflate(table, tablesList, this)

                tableItem.reactive().clicks.bind(this) {
                    val intent = TableActivity.intent(this, table)
                    startActivity(intent)
                }

                tablesList.addView(tableItem)
            }
        }
    }

}
