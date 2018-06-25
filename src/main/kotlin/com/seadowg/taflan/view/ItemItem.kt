package com.seadowg.taflan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import com.google.android.material.card.MaterialCardView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.lifecycle
import com.seadowg.taflan.util.reactive

class ItemItem : MaterialCardView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var table: Table.Existing
    private lateinit var item: Item.Existing
    private lateinit var tableRepository: ReactiveTableRepository

    private lateinit var popup: PopupMenu

    private fun initialize() {
        val menuButton = findViewById<View>(R.id.menu)
        popup = PopupMenu(context, menuButton)
        popup.inflate(R.menu.item_menu)

        menuButton.reactive().clicks.bind(lifecycle()) {
            popup.show()
        }

        popup.setOnMenuItemClickListener {
            deleteItem()
            true
        }
    }

    fun setItem(item: Item.Existing, table: Table.Existing, tableRepository: ReactiveTableRepository): ItemItem {
        this.item = item
        this.table = table
        this.tableRepository = tableRepository

        val fieldsList = findViewById<ViewGroup>(R.id.fields)
        fieldsList.removeAllViews()

        item.values.forEachIndexed { index, value ->
            val itemField = LayoutInflater.from(context).inflate(R.layout.item_field, fieldsList, false)

            val nameView = itemField.findViewById<TextView>(R.id.name)
            val valueView = itemField.findViewById<TextView>(R.id.value)

            nameView.text = table.fields[index]
            valueView.text = value
            fieldsList.addView(itemField)
        }

        return this
    }

    private fun deleteItem() {
        tableRepository.change { it.deleteItem(table, item) }
    }

    companion object {
        fun inflate(rootView: ViewGroup?, context: Context): ItemItem {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_item, rootView, false) as ItemItem

            view.initialize()
            return view
        }
    }
}