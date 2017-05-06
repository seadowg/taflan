package com.seadowg.taflan.domain.usecase

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.sample

class TableCreator(val tableRepository: TableRepository) {

    fun create(name: String): Table.Existing {
        val lastTable = tableRepository.fetchAll().lastOrNull()

        val color = if (lastTable != null) {
            (Color.values().toList() - lastTable.color).sample()
        } else {
            Color.values().sample()
        }

        return tableRepository.create(Table.New(name, color, listOf("Name")))
    }
}