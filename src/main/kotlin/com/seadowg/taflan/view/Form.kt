package com.seadowg.taflan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.seadowg.taflan.R
import com.seadowg.taflan.util.reactive

class Form : FrameLayout {

    constructor(context: Context) : super(context) {
        inflate(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        inflate(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        inflate(context)
    }

    fun setup(fields: List<Field>, submitText: String, onSubmit: (List<String>) -> Unit) {
        val editTexts = renderFields(fields)

        val submitButton = findViewById(R.id.submit) as Button
        submitButton.text = submitText

        val isValids = editTexts.map { it.reactive().text.map(String::isNotEmpty) }
        val allAreValid = isValids.first().zipN(isValids.drop(1)).map { !it.contains(false) }
        submitButton.reactive().enabled = allAreValid

        submitButton.reactive().clicks.bind(this) {
            val values = editTexts.map { field -> field.text.toString() }
            onSubmit(values)
        }
    }

    private fun renderFields(fields: List<Field>): List<EditText> {
        val fieldList = findViewById(R.id.fields) as ViewGroup

        val editTexts = fields.map { (name, value) ->
            val editText = LayoutInflater.from(context).inflate(R.layout.field_entry, fieldList, false) as EditText
            editText.hint = name
            editText.setText(value)

            editText
        }

        editTexts.forEach { fieldList.addView(it) }
        return editTexts
    }

    private fun inflate(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.form, this)
    }

    data class Field(val name: String, val value: String)
}