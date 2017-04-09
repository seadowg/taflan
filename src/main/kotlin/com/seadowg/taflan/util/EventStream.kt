package com.seadowg.taflan.util

import java.util.Collections.list

class EventStream<T> {

    private val bindings = mutableMapOf<Any, MutableList<(T) -> Unit>>()

    fun occur(value: T) {
        bindings.values.forEach { callbacks ->
            callbacks.forEach { callback ->
                callback(value)
            }
        }
    }

    fun bind(reference: Any, callback: (T) -> Unit) {
        bindings.getOrPut(reference) { mutableListOf() }.add(callback)
    }

    fun unbind(reference: Any) {
        bindings.remove(reference)
    }

    fun <U> map(func: (T) -> U): EventStream<U> {
        val newEventStream = EventStream<U>()

        this.bind(newEventStream) { value ->
            newEventStream.occur(func(value))
        }

        return newEventStream
    }

    fun <U> zip(other: EventStream<U>): EventStream<Pair<T, U>> {
        return ZipBridge(this, other).eventStream
    }

    fun zipN(eventStreams: List<EventStream<T>>): EventStream<List<T>> {
        return eventStreams.fold(this.map { listOf(it) }) { left, right ->
            left.zip(right).map { (list, value) -> list + value }
        }
    }

    private class ZipBridge<T, U>(val left: EventStream<T>, val right: EventStream<U>) {
        val eventStream = EventStream<Pair<T, U>>()

        var leftEvent: T? = null
        var rightEvent: U? = null

        init {
            left.bind(eventStream) { value ->
                leftEvent = value

                val otherEvent = rightEvent

                if (otherEvent != null) {
                    eventStream.occur(Pair(value, otherEvent))
                }
            }

            right.bind(eventStream) { value ->
                rightEvent = value

                val otherEvent = leftEvent

                if (otherEvent != null) {
                    eventStream.occur(Pair(otherEvent, value))
                }
            }
        }
    }
}
