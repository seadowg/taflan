package com.seadowg.taflan.test.domain.usecase

import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.repository.InMemoryTableRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.StringReader

class TableCreatorTest {

    @Test
    fun createFromCSV_whenThereIsANameFieldInCSV_worksCorrectly() {
        val repository = InMemoryTableRepository()
        val reader = StringReader("Name,Text\nhello,hi\ngoodbye,see ya").buffered()

        val table = TableCreator(repository).createTableFromCSV("My Table", reader)
        assertThat(table.fields).isEqualTo(listOf("Name", "Text"))
        assertThat(table.items.first().values).isEqualTo(listOf("hello", "hi"))
    }
}