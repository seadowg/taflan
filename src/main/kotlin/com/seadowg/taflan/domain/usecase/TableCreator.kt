package com.seadowg.taflan.domain.usecase

import com.seadowg.taflan.csv.CSVParser
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.sample
import java.io.BufferedReader

class TableCreator(val tableRepository: TableRepository) {

    fun create(name: String): Table.Existing {
        return tableRepository.create(Table.New(name, generateColor(), listOf("Name")))
    }

    private fun generateColor(): Color {
        val lastTableColor = tableRepository.fetchAll().lastOrNull()?.color
        val color = (lastTableColor?.let { Color.set() - it } ?: Color.set()).sample()
        return color
    }

    fun createTableFromCSV(name: String, reader: BufferedReader): Table.Existing {
        val result = CSVParser().parse(reader)

        when (result) {
            is CSVParser.Result.Valid -> {
                return tableRepository.create(Table.New(name, generateColor(), emptyList())).let {
                    result.csv.headers.fold(it) { table, field ->
                        tableRepository.addField(table, field)
                    }
                }.let {
                    result.csv.rows.fold(it) { table, row ->
                        tableRepository.addItem(table, Item.New(row))
                    }
                }
            }

            else -> {
                throw RuntimeException("Table could not be created!")
            }
        }
    }
}