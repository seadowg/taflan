package com.seadowg.taflan.adapter

import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.Navigator
import com.seadowg.taflan.view.ItemItem

class ItemAdapter(private val context: AppCompatActivity, val tableRepository: TableRepository, private val tableID: String, private val navigator: Navigator) : androidx.recyclerview.widget.RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(val view: ItemItem) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view)

    private lateinit var table: Table.Existing

    init {
        update()
    }

    fun update() {
        table = tableRepository.fetch(tableID)!!
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemItem.inflate(parent, context))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = table.items[position]

        holder.view.setItem(item, table, tableRepository, onDeleteClicked = {
            tableRepository.deleteItem(table, item)
            update()
        })

        holder.view.setOnClickListener {
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