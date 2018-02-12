package com.seadowg.taflan.activity

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.csv.CSVParser
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

        val form = findViewById<Form>(R.id.form)
        form.setup(listOf(Form.Field("Name", "", true)), "Add") { values ->
            TableCreator(tableRepository).createTableFromCSV(
                    values.first(),
                    contentReader.read(intent.data).bufferedReader()
            )

            finish()
        }
    }
}