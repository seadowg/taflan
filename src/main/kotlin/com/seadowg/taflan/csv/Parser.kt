package com.seadowg.taflan.csv

import java.io.BufferedReader

class Parser {
    fun parse(reader: BufferedReader): CSV {
        return reader.lineSequence().fold(CSV(emptyList(), emptyList())) { csv, line ->
            val split = line.split(",")

            if (csv.headers.isEmpty()) {
                csv.copy(headers = split)
            } else {
                csv.copy(rows = csv.rows + listOf(split))
            }
        }
    }
}
