package com.seadowg.taflan.domain

import java.io.Serializable

data class Table(val name: String, val color: Color, val fields: List<String>, val items: List<Item> = emptyList()) : Serializable {

    val id = name
}