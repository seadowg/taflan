package com.seadowg.taflan.test.contract

import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

abstract class TableRepositoryTest {

    abstract val tableRepository: TableRepository

    @Test
    fun fetch_fetchesTableByID() {
        val table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))
        val fetchedTable = tableRepository.fetch(table.id)

        assertThat(fetchedTable).isEqualTo(table)
    }

    @Test
    fun create_generatesUniqueIDsForEachTable() {
        val table1 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))
        val table2 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))
        val table3 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        assertThat(table1.id).isNotEqualTo(table2.id)
        assertThat(table2.id).isNotEqualTo(table3.id)
        assertThat(table1.id).isNotEqualTo(table3.id)
    }

    @Test
    fun fetchAll_returnsAllTables() {
        val table1 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))
        val table2 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))
        val table3 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        val all = tableRepository.fetchAll()
        assertThat(all.size).isEqualTo(3)
        assertThat(all.contains(table1))
        assertThat(all.contains(table2))
        assertThat(all.contains(table3))
    }

    @Test
    fun addItem_addsAnItemToTheTable() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        table = tableRepository.addItem(table, Item.New(listOf("Long John Silver", "67cm")))
        table = tableRepository.addItem(table, Item.New(listOf("Jack Sparrow", "12cm")))

        val tableWithItems = tableRepository.fetch(table.id)
        assertThat(tableWithItems.items.size).isEqualTo(2)
        assertThat(tableWithItems.items.find { it.values == listOf("Long John Silver", "67cm") }).isNotNull()
        assertThat(tableWithItems.items.find { it.values == listOf("Jack Sparrow", "12cm") }).isNotNull()
    }

    @Test
    fun addItem_generatesUniqueIDsForEachItem() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        table = tableRepository.addItem(table, Item.New(listOf("Geoff")))
        table = tableRepository.addItem(table, Item.New(listOf("Troy")))
        table = tableRepository.addItem(table, Item.New(listOf("Charles")))

        val items = tableRepository.fetch(table.id).items
        assertThat(items[0].id).isNotEqualTo(items[1].id)
        assertThat(items[1].id).isNotEqualTo(items[2].id)
        assertThat(items[0].id).isNotEqualTo(items[2].id)
    }

    @Test
    fun addField_addsAFieldToATable() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        table = tableRepository.addField(table, "Beard Length")
        table = tableRepository.addField(table, "Ship Name")

        val tableWithNewFields = tableRepository.fetch(table.id)
        assertThat(tableWithNewFields.fields.size).isEqualTo(3)
        assertThat(tableWithNewFields.fields[0]).isEqualTo("Name")
        assertThat(tableWithNewFields.fields[1]).isEqualTo("Beard Length")
        assertThat(tableWithNewFields.fields[2]).isEqualTo("Ship Name")
    }

    @Test
    fun addField_addsTheFieldToExistingItems() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        table = tableRepository.addItem(table, Item.New(listOf("Long John Silver")))

        table = tableRepository.addField(table, "Beard Length")
        table = tableRepository.addField(table, "Ship Name")

        val tableWithNewFields = tableRepository.fetch(table.id)
        assertThat(tableWithNewFields.items.first().values.size).isEqualTo(3)
        assertThat(tableWithNewFields.items.first().values[0]).isEqualTo("Long John Silver")
        assertThat(tableWithNewFields.items.first().values[1]).isEqualTo("")
        assertThat(tableWithNewFields.items.first().values[2]).isEqualTo("")
    }

    @Test
    fun updateItem_updatesTheItemInTheTable() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        table = tableRepository.addItem(table, Item.New(listOf("Geoff")))
        table = tableRepository.addItem(table, Item.New(listOf("Troy")))
        table = tableRepository.addItem(table, Item.New(listOf("Charles")))

        val tableWithItems = tableRepository.fetch(table.id)
        val changedItem = Item.Existing(
                tableWithItems.items[1].id,
                listOf("Kelly")
        )

        tableRepository.updateItem(tableWithItems, changedItem)

        val updatedItems = tableRepository.fetch(tableWithItems.id).items
        assertThat(updatedItems.single { it.id == changedItem.id }.values).isEqualTo(listOf("Kelly"))
        assertThat(updatedItems.find { it.values == listOf("Geoff") }).isNotNull()
        assertThat(updatedItems.find { it.values == listOf("Charles") }).isNotNull()
    }

    @Test
    fun deleteItem_removesTheItemFromTheTable() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        table = tableRepository.addItem(table, Item.New(listOf("Geoff")))
        table = tableRepository.addItem(table, Item.New(listOf("Troy")))

        val geoff = tableRepository.fetch(table.id).items.single { it.values == listOf("Geoff") }
        tableRepository.deleteItem(table, geoff)

        val tableWithoutGeoff = tableRepository.fetch(table.id)
        assertThat(tableWithoutGeoff.items.size).isEqualTo(1)
        assertThat(tableWithoutGeoff.items.first().values).isEqualTo(listOf("Troy"))
    }
}
