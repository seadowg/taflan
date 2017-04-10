package com.seadowg.taflan.test

import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.test.contract.TableRepositoryTest

class InMemoryTableRepositoryTest : TableRepositoryTest() {

    override val tableRepository: TableRepository = InMemoryTableRepository()
}
