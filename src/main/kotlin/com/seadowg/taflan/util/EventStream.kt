package com.seadowg.taflan.util

class EventStream<T> {

    private val bindings = mutableMapOf<Any, MutableList<(Long, T) -> Unit>>()

    fun occur(time: Long, value: T) {
        bindings.values.forEach { callbacks ->
            callbacks.forEach { callback ->
                callback(time, value)
            }
        }
    }

    fun bind(reference: Any, callback: (Long, T) -> Unit) {
        bindings.getOrPut(reference) { mutableListOf() }.add(callback)
    }

    fun unbind(reference: Any) {
        bindings.remove(reference)
    }

    fun <U> map(func: (T) -> U): EventStream<U> {
        val eventStream = EventStream<U>()

        this.bind(eventStream) { time, value ->
            eventStream.occur(time, func(value))
        }

        return eventStream
    }
}
