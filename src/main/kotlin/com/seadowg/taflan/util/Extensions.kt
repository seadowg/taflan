package com.seadowg.taflan.util

import java.util.*

fun <T> List<T>.sample(): T {
    return this[Random().nextInt(this.size)]
}

fun <T> Array<T>.sample(): T {
    return this.toList().sample()
}

fun <T> Set<T>.sample(): T {
    return this.toList().sample()
}
