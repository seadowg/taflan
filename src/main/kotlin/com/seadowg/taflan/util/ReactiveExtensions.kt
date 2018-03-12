package com.seadowg.taflan.util

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun <T> Bindable<T>.bind(lifecycleOwner: LifecycleOwner, callback: (T) -> Unit) {
    val reference = object : Reference, LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun onResume() {
            bind(this, callback)
        }


        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun onPause() {
            unbind(this)
        }
    }

    lifecycleOwner.lifecycle.addObserver(reference)
}

fun EditText.reactive(): EditTextReactive {
    return EditTextReactive(this)
}

fun View.reactive(): ViewReactive {
    return ViewReactive(this)
}

class ViewReactive(private val view: View) {
    var enabled: Reactive<Boolean> = Reactive(view.isEnabled, EventStream())
        set(value) {
            field = value
            field.bind(view.lifecycle()) { value -> view.isEnabled = value }
        }

    val clicks: EventStream<Unit>
        get() {
            val eventStream = EventStream<Unit>()
            view.setOnClickListener { eventStream.occur(Unit) }
            return eventStream
        }
}

class EditTextReactive(private val editText: EditText) {
    val text: Reactive<String>
        get() {
            val eventStream = EventStream<String>()

            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable) {
                    eventStream.occur(s.toString())
                }

                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                }
            })

            return Reactive(editText.text.toString(), eventStream)
        }
}
