package com.seadowg.taflan.activity

import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.view.Form

class ImportCSVActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_table)
        setupToolbar("Import Table")

        val form = findViewById(R.id.form) as Form
        form.setup(listOf(Form.Field("Name", "")), "Add") { values ->
            val reader = contentResolver.openInputStream(intent.data).bufferedReader()

            val fieldsAndItems = reader.lineSequence().fold(Pair(emptyList<String>(), emptyList<Item.New>())) { fieldsAndItems, line ->
                val values = line.split(",")

                if (fieldsAndItems.first.isEmpty()) {
                    Pair(values, fieldsAndItems.second)
                } else {
                    Pair(fieldsAndItems.first, fieldsAndItems.second + Item.New(values))
                }
            }

            TableCreator(tableRepository).create(values.first()).let {
                fieldsAndItems.first.fold(it) { table, field ->
                    tableRepository.addField(table, field)
                }
            }.let {
                fieldsAndItems.second.fold(it) { table, item ->
                    tableRepository.addItem(table, item)
                }
            }

            finish()
        }
    }
}