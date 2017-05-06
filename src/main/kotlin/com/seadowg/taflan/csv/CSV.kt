package com.seadowg.taflan.csv

data class CSV(val headers: List<String>, val rows: List<List<String>>) {

    fun isValid(): Boolean {
        return rows.any { it.size != headers.size }
    }

    override fun toString(): String {
        val allRows = listOf(headers) + rows
        return allRows.map { it.joinToString(",") }.joinToString("\n") + "\n"
    }
}