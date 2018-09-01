package com.seadowg.taflan.view

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.seadowg.taflan.R

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

        val submitButton = findViewById<Button>(R.id.submit)
        submitButton.text = submitText

        val validator = Validator(submitButton, editTexts) { fields -> !fields.any { it.text.isEmpty() } }
        editTexts.forEach { it.addTextChangedListener(validator) }

        submitButton.setOnClickListener {
            val values = editTexts.map { field -> field.text.toString() }
            onSubmit(values)
        }

        prefillFields(editTexts, fields)
    }

    private fun prefillFields(editTexts: List<EditText>, fields: List<Field>) {
        editTexts.zip(fields).forEach { (editText, field) ->
            editText.setText(field.value)
        }
    }

    private fun renderFields(fields: List<Field>): List<EditText> {
        val fieldList = findViewById<ViewGroup>(R.id.fields)

        val editTexts = fields.map { (name, _, multiline) ->
            val editText = if (multiline) {
                LayoutInflater.from(context).inflate(R.layout.field_entry_multiline, fieldList, false) as EditText
            } else {
                LayoutInflater.from(context).inflate(R.layout.field_entry, fieldList, false) as EditText
            }

            editText.hint = name

            editText
        }

        editTexts.forEach { fieldList.addView(it) }
        return editTexts
    }

    private fun inflate(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.form, this)
    }

    data class Field(val name: String, val value: String, val multiline: Boolean = true)
}

open class Validator(
        private val submitButton: Button,
        private val fields: List<EditText>,
        private val isValid: (List<EditText>) -> Boolean) : TextWatcher {

    override fun afterTextChanged(s: Editable) {
        submitButton.isEnabled = isValid(fields)
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }
}
