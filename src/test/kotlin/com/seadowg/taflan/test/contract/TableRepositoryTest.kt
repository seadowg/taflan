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
    fun fetch_whenTableDoesntExist_returnsNull() {
        val fetchedTable = tableRepository.fetch("does not exist")
        assertThat(fetchedTable).isNull()
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
    fun fetchItems_returnsItemsForTable() {
        val table1 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))
        val table2 = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        val item1 = tableRepository.addItem(table1.id, Item.New(listOf("Black Beard", "Long")))
        val item2 = tableRepository.addItem(table1.id, Item.New(listOf("Long John Silver", "Short")))

        tableRepository.addItem(table2.id, Item.New(listOf("Jack Sparrow", "Medium")))

        val items = tableRepository.fetchItems(table1.id)
        assertThat(items.size).isEqualTo(2)
        assertThat(items.contains(item1))
        assertThat(items.contains(item2))
    }

    @Test
    fun addItem_addsAnItemToTheTable() {
        val table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        tableRepository.addItem(table.id, Item.New(listOf("Long John Silver", "67cm")))
        tableRepository.addItem(table.id, Item.New(listOf("Jack Sparrow", "12cm")))

        val tableWithItems = tableRepository.fetch(table.id)!!
        assertThat(tableWithItems.items.size).isEqualTo(2)
        assertThat(tableWithItems.items.find { it.values == listOf("Long John Silver", "67cm") }).isNotNull()
        assertThat(tableWithItems.items.find { it.values == listOf("Jack Sparrow", "12cm") }).isNotNull()
    }

    @Test
    fun addItem_generatesUniqueIDsForEachItem() {
        val table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name", "Beard Length")))

        tableRepository.addItem(table.id, Item.New(listOf("Geoff")))
        tableRepository.addItem(table.id, Item.New(listOf("Troy")))
        tableRepository.addItem(table.id, Item.New(listOf("Charles")))

        val items = tableRepository.fetch(table.id)!!.items
        assertThat(items[0].id).isNotEqualTo(items[1].id)
        assertThat(items[1].id).isNotEqualTo(items[2].id)
        assertThat(items[0].id).isNotEqualTo(items[2].id)
    }

    @Test
    fun addField_addsAFieldToATable() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        table = tableRepository.addField(table.id, "Beard Length")
        table = tableRepository.addField(table.id, "Ship Name")

        val tableWithNewFields = tableRepository.fetch(table.id)!!
        assertThat(tableWithNewFields.fields.size).isEqualTo(3)
        assertThat(tableWithNewFields.fields[0]).isEqualTo("Name")
        assertThat(tableWithNewFields.fields[1]).isEqualTo("Beard Length")
        assertThat(tableWithNewFields.fields[2]).isEqualTo("Ship Name")
    }

    @Test
    fun addField_addsTheFieldToExistingItems() {
        var table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        tableRepository.addItem(table.id, Item.New(listOf("Long John Silver")))

        table = tableRepository.addField(table.id, "Beard Length")
        table = tableRepository.addField(table.id, "Ship Name")

        val tableWithNewFields = tableRepository.fetch(table.id)!!
        assertThat(tableWithNewFields.items.first().values.size).isEqualTo(3)
        assertThat(tableWithNewFields.items.first().values[0]).isEqualTo("Long John Silver")
        assertThat(tableWithNewFields.items.first().values[1]).isEqualTo("")
        assertThat(tableWithNewFields.items.first().values[2]).isEqualTo("")
    }

    @Test
    fun updateItem_updatesTheItemInTheTable() {
        val table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        tableRepository.addItem(table.id, Item.New(listOf("Geoff")))
        tableRepository.addItem(table.id, Item.New(listOf("Troy")))
        tableRepository.addItem(table.id, Item.New(listOf("Charles")))

        val tableWithItems = tableRepository.fetch(table.id)
        val changedItem = Item.Existing(
                tableWithItems!!.items[1].id,
                listOf("Kelly")
        )

        tableRepository.updateItem(tableWithItems, changedItem)

        val updatedItems = tableRepository.fetch(tableWithItems.id)!!.items
        assertThat(updatedItems.single { it.id == changedItem.id }.values).isEqualTo(listOf("Kelly"))
        assertThat(updatedItems.find { it.values == listOf("Geoff") }).isNotNull()
        assertThat(updatedItems.find { it.values == listOf("Charles") }).isNotNull()
    }

    @Test
    fun deleteItem_removesTheItemFromTheTable() {
        val table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))

        tableRepository.addItem(table.id, Item.New(listOf("Geoff")))
        tableRepository.addItem(table.id, Item.New(listOf("Troy")))

        val geoff = tableRepository.fetch(table.id)!!.items.single { it.values == listOf("Geoff") }
        tableRepository.deleteItem(table.id, geoff)

        val tableWithoutGeoff = tableRepository.fetch(table.id)!!
        assertThat(tableWithoutGeoff.items.size).isEqualTo(1)
        assertThat(tableWithoutGeoff.items.first().values).isEqualTo(listOf("Troy"))
    }

    @Test
    fun delete_removesTable() {
        val table = tableRepository.create(Table.New("Favourite Pirates", Color.Red, listOf("Name")))
        tableRepository.delete(table)

        val tables = tableRepository.fetchAll()
        assertThat(tables.find { it.id == table.id }).isNull()
    }
}
