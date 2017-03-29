package com.seadowg.taflan.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.colorDrawable

class NewFieldActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_field)

        val table = intent.extras.getSerializable(EXTRA_TABLE) as Table
        setupToolbar("Add Field", table.colorDrawable(this))

        val nameField = findViewById(R.id.name) as EditText
        val addButton = findViewById(R.id.add).reactive()

        val nameNotEmpty = nameField.reactive().text.map { !it.isEmpty() }
        addButton.enabled = nameNotEmpty

        findViewById(R.id.add).reactive().clicks.bind(this) { _, _ ->
            tableRepository.addField(table, nameField.text.toString())
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}