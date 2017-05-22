package com.seadowg.taflan.domain.usecase

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.sample

class TableCreator(val tableRepository: TableRepository) {

    fun create(name: String): Table.Existing {
        val lastTableColor = tableRepository.fetchAll().lastOrNull()?.color
        val availableColors = lastTableColor?.let { Color.set() - it } ?: Color.set()

        return tableRepository.create(Table.New(name, availableColors.sample(), listOf("Name")))
    }
}