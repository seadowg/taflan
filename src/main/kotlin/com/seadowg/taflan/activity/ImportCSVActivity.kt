package com.seadowg.taflan.activity

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.csv.Parser
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.ContentReader
import com.seadowg.taflan.view.Form
import java.io.BufferedReader

class ImportCSVActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()
    private val contentReader: ContentReader by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_table)
        setupToolbar("Import Table")

        val form = findViewById(R.id.form) as Form
        form.setup(listOf(Form.Field("Name", "")), "Add") { values ->
            createTableFromCSV(values.first(), contentReader.read(intent.data).bufferedReader())
            finish()
        }
    }

    private fun createTableFromCSV(name: String, reader: BufferedReader) {
        val csv = Parser().parse(reader)

        TableCreator(tableRepository).create(name).let {
            csv.headers.fold(it) { table, field ->
                tableRepository.addField(table, field)
            }
        }.let {
            csv.rows.fold(it) { table, row ->
                tableRepository.addItem(table, Item.New(row))
            }
        }
    }
}