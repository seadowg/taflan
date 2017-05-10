package com.seadowg.taflan.csv

import org.apache.commons.csv.CSVFormat
import java.io.BufferedReader

class CSVParser {
    fun parse(reader: BufferedReader): Result {
        val csv = CSVFormat.RFC4180.parse(reader).fold(CSV(emptyList(), emptyList())) { csv, line ->
            val values = line.map { it }

            if (csv.headers.isEmpty()) {
                csv.copy(headers = values)
            } else {
                csv.copy(rows = csv.rows + listOf(values))
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
