package com.seadowg.taflan.activity

import android.os.Bundle
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.colorDrawable

class NewItemActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_item)

        val table = intent.getSerializableExtra(EXTRA_TABLE) as Table
        setupToolbar("Add Item", color = table.colorDrawable(this))

        findViewById(R.id.add).reactive().clicks.bind(this) { _, _  ->
            val nameField = findViewById(R.id.name) as TextView

            tableRepository.addItem(table, Item(nameField.text.toString()))
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}