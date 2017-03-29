package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.view.TableItem

class LaunchActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch)
        setupToolbar("Taflan")

        findViewById(R.id.fab).setOnClickListener {
            startActivity(Intent(this, NewTableActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val tablesList = findViewById(R.id.tables) as ViewGroup
        tablesList.removeAllViews()

        tableRepository.fetchAll().forEach {
            tablesList.addView(TableItem.inflate(it, tablesList, this))
        }
    }
}
