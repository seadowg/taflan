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
        return (lastTableColor?.let { Color.set() - it } ?: Color.set()).sample()
    }

    fun createTableFromCSV(name: String, reader: BufferedReader): Table.Existing {
        val result = CSVParser().parse(reader)

        when (result) {
            is CSVParser.Result.Valid -> {
                val table = tableRepository.create(Table.New(name, generateColor(), emptyList())).let {
                    result.csv.headers.fold(it) { table, field ->
                        tableRepository.addField(table.id, field)
                    }
                }.apply {
                    result.csv.rows.forEach { row ->
                        tableRepository.addItem(id, Item.New(row))
                    }
                }

                return tableRepository.fetch(table.id)!!
            }

            else -> {
                throw RuntimeException("Table could not be created!")
            }
        }
    }
}