package com.seadowg.taflan.activity

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.view.Form

class NewTableActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_table)
        setupToolbar("Add Table", backArrow = true)

        val form = findViewById<Form>(R.id.form)
        form.setup(listOf(Form.Field("Name", "", multiline = false)), "Add") { values ->
            tableRepository.change { TableCreator(it).create(values.first()) }
            finish()
        }
    }
}
