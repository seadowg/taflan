package com.seadowg.taflan.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive

class EditItemActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_item)

        val table = intent.getSerializableExtra(EXTRA_TABLE) as Table
        val item = intent.getSerializableExtra(EXTRA_ITEM) as Item.Existing

        setupToolbar(item.values[0])

        val fieldList = findViewById(R.id.fields) as ViewGroup

        val fields = table.fields.zip(item.values).map { (field, value) ->
            val editText = LayoutInflater.from(this).inflate(R.layout.field_entry, fieldList, false) as EditText
            editText.hint = field
            editText.setText(value)
            editText
        }

        fields.forEach { fieldList.addView(it) }

        findViewById(R.id.update).reactive().clicks.bind(this) { _, _ ->
            val values = fields.map { field -> field.text.toString() }
            tableRepository.updateItem(table, Item.Existing(item.id, values))
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
        val EXTRA_ITEM = "extra_item"
    }
}