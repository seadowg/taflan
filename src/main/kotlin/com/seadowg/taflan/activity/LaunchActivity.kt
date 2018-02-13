package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Reference
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.TableItem

class LaunchActivity : TaflanActivity(), Reference {

    private val tableRepository: ReactiveTableRepository by injector.instance()
    private val tables by lazy { tableRepository.fetchAll() }

    private val fab by lazy { findViewById<View>(R.id.fab).reactive() }
    private val tablesList by lazy { findViewById<ViewGroup>(R.id.tables) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch)
        setupToolbar("Taflan")
    }

    override fun onResume() {
        super.onResume()

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
