package com.seadowg.taflan.activity

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.view.Form
import com.seadowg.taflan.view.colorDrawable

class EditItemActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_item)

        val table = intent.getSerializableExtra(EXTRA_TABLE) as Table.Existing
        val item = intent.getSerializableExtra(EXTRA_ITEM) as Item.Existing

        setupToolbar(item.values[0], color = table.colorDrawable(this), backArrow = true)

        val fields = table.fields.zip(item.values) { name, value -> Form.Field(name, value, true) }

        val form = findViewById<Form>(R.id.form)
        form.setup(fields, "Update") { values ->
            tableRepository.change { it.updateItem(table, Item.Existing(item.id, values)) }
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
        val EXTRA_ITEM = "extra_item"
    }
}