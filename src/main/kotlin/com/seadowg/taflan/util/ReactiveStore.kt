package com.seadowg.taflan.util

open class ReactiveStore<T : Identifiable, out Y: Store<T>>(val store: Y) {

    private val idEventStreams: MutableMap<String, EventStream<T>> = mutableMapOf()
    private val allEventStream = EventStream<List<T>>()

    fun fetch(id: String): Reactive<T> {
        val eventStream = idEventStreams.getOrPut(id) { EventStream() }
        return Reactive(store.fetch(id), eventStream)
    }

    fun change(changer: (Y) -> T) {
        val newValue = changer(store)
        idEventStreams[newValue.id]?.occur(newValue)
        allEventStream.occur(store.fetchAll())
    }

    fun fetchAll(): Reactive<List<T>> {
        return Reactive(store.fetchAll(), allEventStream)
    }
}

interface Identifiable {
    val id: String
}

interface Store<out T> {
    fun fetch(id: String): T
    fun fetchAll(): List<T>
}
