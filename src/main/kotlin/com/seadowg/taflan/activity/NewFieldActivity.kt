package com.seadowg.taflan.activity

import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.Form
import com.seadowg.taflan.view.colorDrawable

class NewFieldActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_field)

        val table = intent.extras.getSerializable(EXTRA_TABLE) as Table.Existing
        setupToolbar("Add Field", table.colorDrawable(this), backArrow = true)

        val form = findViewById(R.id.form) as Form
        form.setup(listOf(Form.Field("Name", "")), "Add") { values ->
            tableRepository.addField(table, values.first())
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}