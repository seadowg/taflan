package com.seadowg.taflan.activity

import android.os.Bundle
import android.widget.EditText
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.util.sample

class NewTableActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_table)
        setupToolbar("Add Table")

        val nameField = findViewById(R.id.name) as EditText
        val addButton = findViewById(R.id.add).reactive()

        val nameNotEmpty = nameField.reactive().text.map { !it.isEmpty() }
        addButton.enabled = nameNotEmpty

        addButton.clicks.bind(this) { _, _ ->
            tableRepository.create(nameField.text.toString(), Color.ALL.sample())
            finish()
        }
    }
}
