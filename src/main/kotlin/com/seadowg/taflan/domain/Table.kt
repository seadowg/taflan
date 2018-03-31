package com.seadowg.taflan.domain

import com.seadowg.taflan.util.Identifiable
import java.io.Serializable

sealed class Table : Serializable {

    abstract val name: String
    abstract val color: Color
    abstract val fields: List<String>
    abstract val items: List<Item.Existing>

    data class New(
            override val name: String,
            override val color: Color,
            override val fields: List<String>
    ) : Table() {

        override val items: List<Item.Existing> = emptyList()
    }

    data class Existing(
            override val id: String,
            override val name: String,
            override val color: Color,
            override val fields: List<String>,
            override val items: List<Item.Existing>
    ) : Table(), Identifiable
}