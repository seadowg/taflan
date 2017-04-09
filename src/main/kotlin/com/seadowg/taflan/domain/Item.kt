package com.seadowg.taflan.domain

import java.io.Serializable

sealed class Item : Serializable {

    abstract val values: List<String>

    data class New(override val values: List<String>) : Item()
    data class Existing(val id: String, override val values: List<String>) : Item()
}