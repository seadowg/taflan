package com.seadowg.taflan.util

open class ReactiveStore<T : Identifiable, out Y: Store<T>>(val store: Y) {

    private val idEventStreams: MutableMap<String, EventStream<T>> = mutableMapOf()
    private val allEventStream = EventStream<List<T>>()

    fun fetch(id: String): Reactive<T>? {
        val eventStream = idEventStreams.getOrPut(id) { EventStream() }

        return store.fetch(id)?.let {
            Reactive(it, eventStream)
        }
    }

    fun fetchAll(): Reactive<List<T>> {
        return Reactive(store.fetchAll(), allEventStream)
    }

    fun change(changer: (Y) -> Unit) {
        changer(store)
        idEventStreams.entries.forEach { entry ->
            store.fetch(entry.key)?.let { entry.value.occur(it) }
        }

        allEventStream.occur(store.fetchAll())
    }
}

interface Identifiable {
    val id: String
}

interface Store<out T> {
    fun fetch(id: String): T?
    fun fetchAll(): List<T>
}
