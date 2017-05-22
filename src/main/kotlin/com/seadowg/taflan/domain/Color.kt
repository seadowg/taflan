package com.seadowg.taflan.domain

enum class Color {
    Red,
    Blue,
    Green,
    Orange;

    companion object {
        fun set(): Set<Color> {
            return values().toSet()
        }
    }
}
