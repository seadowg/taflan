package com.seadowg.taflan.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.view.TableItem

class FieldsActivity : TaflanActivity() {

    private val table: Table.Existing by lazy {
        (intent.extras.getSerializable(FieldsActivity.EXTRA_TABLE) as Table.Existing)
    }

    private val fields: ViewGroup by lazy { findViewById(R.id.fields) as ViewGroup }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fields)

        table.fields.forEach {
            val view = LayoutInflater.from(this).inflate(R.layout.field_item, fields, false)
            (view.findViewById(R.id.name) as TextView).text = it
            fields.addView(view)
        }
    }

    companion object {
        private val EXTRA_TABLE = "extra_table"
    }
}