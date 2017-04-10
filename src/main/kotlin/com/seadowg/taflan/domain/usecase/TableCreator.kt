package com.seadowg.taflan.domain.usecase

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.sample

class TableCreator(val tableRepository: TableRepository) {

    fun create(name: String) {
        tableRepository.create(Table.New(name, Color.values().sample(), listOf("Name")))
    }
}