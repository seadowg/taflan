package com.seadowg.taflan.util

open class ReactiveStore<T : Identifiable, out Y: Store<T>>(val store: Y) {

    private val eventStreams: MutableMap<String, EventStream<T>> = mutableMapOf()

    fun fetch(id: String): Reactive<T> {
        val eventStream = eventStreams.getOrPut(id) { EventStream() }
        return Reactive(store.fetch(id), eventStream)
    }

    fun change(changer: (Y) -> T) {
        val newValue = changer(store)
        eventStreams[newValue.id]?.occur(newValue)
    }
}

interface Identifiable {
    val id: String
}

interface Store<out T> {
    fun fetch(id: String): T
}
