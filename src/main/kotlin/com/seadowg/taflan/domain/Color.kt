package com.seadowg.taflan.domain

sealed class Color {
    class Red : Color()
    class Blue : Color()
    class Green : Color()
    class Orange : Color()

    companion object {
        val ALL = listOf(Color.Blue(), Color.Red(), Color.Orange(), Color.Green())
    }
}
