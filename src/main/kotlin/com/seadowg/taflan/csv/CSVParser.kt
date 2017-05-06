package com.seadowg.taflan.csv

import java.io.BufferedReader

class CSVParser {
    fun parse(reader: BufferedReader): Result {
        val csv = reader.lineSequence().fold(CSV(emptyList(), emptyList())) { csv, line ->
            val split = line.split(",")

            if (csv.headers.isEmpty()) {
                csv.copy(headers = split)
            } else {
                csv.copy(rows = csv.rows + listOf(split))
            }
        }

        return when {
            csv.headers.isEmpty() -> Result.Empty()
            !csv.isValid() -> Result.Invalid()
            else -> Result.Valid(csv)
        }
    }

    sealed class Result {
        data class Valid(val csv: CSV) : Result()
        class Empty : Result()
        class Invalid : Result()
    }
}
