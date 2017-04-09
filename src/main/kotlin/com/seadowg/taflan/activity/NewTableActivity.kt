package com.seadowg.taflan.activity

import android.os.Bundle
import android.widget.EditText
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.util.sample
import com.seadowg.taflan.view.Form

class NewTableActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_table)
        setupToolbar("Add Table")

        val form = findViewById(R.id.form) as Form
        form.setup(listOf(Form.Field("Name", "")), "Add") { values ->
            tableRepository.create(values.first(), Color.ALL.sample())
            finish()
        }
    }
}
