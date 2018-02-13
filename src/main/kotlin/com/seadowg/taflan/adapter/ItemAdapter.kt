package com.seadowg.taflan.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.util.Reference
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.ItemItem

class ItemAdapter(val context: Context, val tableRepository: TableRepository, val tableID: String, val navigator: Navigator) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(), Reference {

    class ViewHolder(val view: ItemItem) : RecyclerView.ViewHolder(view)

    private val table: Table.Existing
        get() {
            return tableRepository.fetch(tableID)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemItem.inflate(parent, context, this))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = table.items[position]

        holder.view.setItem(item, table, tableRepository)
        holder.view.reactive().clicks.bind(this) {
            navigator.editItem(table, item)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return table.items.size
    }

}