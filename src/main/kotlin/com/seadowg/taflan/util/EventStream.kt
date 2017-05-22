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

    private class ZipBridge<T, U>(left: EventStream<T>, right: EventStream<U>) {
        val eventStream = EventStream<Pair<T, U>>()

        var leftEvent: T? = null
        var rightEvent: U? = null

        init {
            left.bind(eventStream) { value ->
                leftEvent = value

                rightEvent?.let {
                    eventStream.occur(Pair(value, it))
                }
            }

            right.bind(eventStream) { value ->
                rightEvent = value

                leftEvent?.let {
                    eventStream.occur(Pair(it, value))
                }
            }
        }
    }
}

class Reactive<T>(initial: T, private val eventStream: EventStream<T>) {

    var currentValue = initial

    init {
        eventStream.bind(this) { value ->
            currentValue = value
        }
    }

    fun bind(reference: Any, callback: (T) -> Unit) {
        callback(currentValue)
        eventStream.bind(reference, callback)
    }

    fun unbind(reference: Any) {
        eventStream.unbind(reference)
    }

    fun <U> map(func: (T) -> U): Reactive<U> {
        return Reactive(func(currentValue), eventStream.map(func))
    }

    fun <U> mapN(reactives: List<Reactive<T>>, func: (List<T>) -> U): Reactive<U> {
        val nCurrentVals = (listOf(this) + reactives).map { it.currentValue }
        val zippedStreams = this.eventStream.zipN(reactives.map { it.eventStream })
        val eventStream = zippedStreams.map { values -> func(values) }

        return Reactive(func(nCurrentVals), eventStream)
    }
}
