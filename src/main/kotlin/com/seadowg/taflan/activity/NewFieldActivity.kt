package com.seadowg.taflan.activity

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.view.Form
import com.seadowg.taflan.view.colorDrawable

class NewFieldActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_field)

        val table = intent.extras.getSerializable(EXTRA_TABLE) as Table.Existing
        setupToolbar("Add Field", table.colorDrawable(this), backArrow = true)

        val form = findViewById<Form>(R.id.form)
        form.setup(listOf(Form.Field("Name", "", true)), "Add") { values ->
            tableRepository.change { it.addField(table, values.first()) }
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}