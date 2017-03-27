package com.seadowg.taflan.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.repository.TableRepository

class LaunchActivity : KodeinActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch)

        findViewById(R.id.fab).setOnClickListener {
            startActivity(Intent(this, NewTableActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()

        val tablesList = findViewById(R.id.tables) as ViewGroup
        tablesList.removeAllViews()

        tableRepository.fetchAll().forEach {
            val tableItem = TextView(this)
            tableItem.text = it.name

            tablesList.addView(tableItem)
        }
    }
}
