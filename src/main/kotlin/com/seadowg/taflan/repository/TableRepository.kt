package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table

interface TableRepository {
    fun create(name: String, color: Color)
    fun fetchAll(): List<Table>
}