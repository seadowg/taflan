package com.seadowg.taflan.util

import java.util.*

fun <T> Array<T>.sample(): T {
    return this[Random().nextInt(this.size)]
}
