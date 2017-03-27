package com.seadowg.taflan.repository

import com.seadowg.taflan.domain.Table

interface TableRepository {
    fun create(name: String)
    fun  fetchAll(): List<Table>
}