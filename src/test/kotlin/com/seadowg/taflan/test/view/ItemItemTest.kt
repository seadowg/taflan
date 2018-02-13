package com.seadowg.taflan.test.view

import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.SimpleAdapter
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.view.ItemItem
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.fakes.RoboMenuItem
import org.robolectric.shadows.ShadowPopupMenu

@RunWith(RobolectricTestRunner::class)
class ItemItemTest {

    @Test @Ignore("Need Robolectric support for v7 PopupMenu")
    fun setItem_resetsDeleteClicks() {
        val tableRepository = InMemoryTableRepository()
        val table = tableRepository.create(Table.New("Table", Color.Blue, listOf("Name"))).let {
            tableRepository.addItem(it, Item.New(listOf("Jeff")))
        }.let {
            tableRepository.addItem(it, Item.New(listOf("Barry")))
        }

        val itemItem = ItemItem.inflate(table.items[0], table, null, RuntimeEnvironment.application, tableRepository, ArrayAdapter<String>(RuntimeEnvironment.application, R.layout.item_item))
        itemItem.setItem(table.items[1], table, tableRepository)

        itemItem.findViewById<View>(R.id.menu).performClick()

        val popupMenu = ShadowPopupMenu.getLatestPopupMenu()
        shadowOf(popupMenu).onMenuItemClickListener.onMenuItemClick(RoboMenuItem(R.id.delete))

        assertThat(tableRepository.fetchAll().size).isEqualTo(1)
    }
}
