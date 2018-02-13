package com.seadowg.taflan.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.util.Reference
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.ItemItem

class ItemAdapter(val context: Context, val tableRepository: TableRepository, val tableID: String, val navigator: Navigator) : BaseAdapter(), Reference {

    private val table: Table.Existing
        get() {
            return tableRepository.fetch(tableID)
        }

    override fun getView(position: Int, recycledView: View?, parent: ViewGroup): View {
        val item = table.items[position]

        val itemItem = if (recycledView == null) {
            ItemItem.inflate(item, table, parent, context, tableRepository, this)
        } else {
            (recycledView as ItemItem).setItem(item, table, tableRepository)
        }

        itemItem.reactive().clicks.bind(this) {
            navigator.editItem(table, item)
        }

        return itemItem
    }

    override fun getItem(position: Int): Any {
        return table.items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return table.items.size
    }

}