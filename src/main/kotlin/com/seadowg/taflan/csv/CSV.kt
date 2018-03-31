package com.seadowg.taflan.csv

data class CSV(val headers: List<String>, val rows: List<List<String>>) {

    fun isValid(): Boolean {
        return rows.all { it.size == headers.size }
    }

    override fun toString(): String {
        return (listOf(headers) + rows).joinToString("\n", postfix = "\n") {
            it.joinToString(",", transform = { s -> "\"$s\"" })
        }
    }
}