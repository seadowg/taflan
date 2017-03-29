package com.seadowg.taflan.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.colorDrawable

class NewItemActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_item)

        val table = intent.getSerializableExtra(EXTRA_TABLE) as Table
        setupToolbar("Add Item", color = table.colorDrawable(this))

        val fieldList = findViewById(R.id.fields) as ViewGroup
        val fields = table.fields.map { field ->
            val editText = LayoutInflater.from(this).inflate(R.layout.field_entry, fieldList, false) as EditText
            editText.hint = field
            editText
        }

        fields.forEach { fieldList.addView(it) }

        findViewById(R.id.add).reactive().clicks.bind(this) { _, _  ->
            val values = fields.map { field -> field.text.toString() }
            tableRepository.addItem(table, Item(values))
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}