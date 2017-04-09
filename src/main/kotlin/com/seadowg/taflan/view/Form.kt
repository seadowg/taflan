package com.seadowg.taflan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
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

    fun setup(fieldsAndValues: List<Pair<String, String>>, submitText: String, onSubmit: (List<String>) -> Unit) {
        val fieldList = findViewById(R.id.fields) as ViewGroup

        val fields = fieldsAndValues.map { (field, value) ->
            val editText = LayoutInflater.from(context).inflate(R.layout.field_entry, fieldList, false) as EditText
            editText.hint = field
            editText.setText(value)
            editText
        }

        fields.forEach { fieldList.addView(it) }

        val submitButton = findViewById(R.id.submit) as Button
        submitButton.text = submitText

        submitButton.reactive().clicks.bind(this) { _, _ ->
            val values = fields.map { field -> field.text.toString() }
            onSubmit(values)
        }
    }

    private fun inflate(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.form, this)
    }
}
