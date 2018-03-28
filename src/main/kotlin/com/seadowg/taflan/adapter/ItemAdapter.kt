package com.seadowg.taflan.adapter

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.ItemItem

class ItemAdapter(private val context: AppCompatActivity, val tableRepository: TableRepository, private val tableID: String, private val navigator: Navigator) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(val view: ItemItem) : RecyclerView.ViewHolder(view)

    private val table: Table.Existing
        get() {
            return tableRepository.fetch(tableID)!!
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemItem.inflate(parent, context, this))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = table.items[position]

        holder.view.setItem(item, table, tableRepository)
        holder.view.reactive().clicks.bind(context) {
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