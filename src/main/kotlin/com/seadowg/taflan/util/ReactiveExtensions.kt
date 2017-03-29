package com.seadowg.taflan.util

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun EditText.reactive(): EditTextReactive {
    return EditTextReactive(this)
}

fun View.reactive(): ViewReactive {
    return ViewReactive(this)
}

class ViewReactive(private val view: View) {
    var enabled: EventStream<Boolean> = EventStream()
        set(value) {
            field.unbind(this)

            field = value
            field.bind(this) { _, value -> view.isEnabled = value }
        }

    val clicks: EventStream<Unit>
        get() {
            val eventStream = EventStream<Unit>()
            view.setOnClickListener { eventStream.occur(System.currentTimeMillis(), Unit) }
            return eventStream
        }
}

class EditTextReactive(private val editText: EditText) {
    val text: EventStream<String>
        get() {
            val eventStream = EventStream<String>()
            eventStream.occur(System.currentTimeMillis(), editText.text.toString())

            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    eventStream.occur(System.currentTimeMillis(), s.toString())
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })

            return eventStream
        }
}
