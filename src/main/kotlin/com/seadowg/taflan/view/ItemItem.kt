package com.seadowg.taflan.view

import android.content.Context
import android.content.Intent
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.activity.EditItemActivity
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.EventStream
import com.seadowg.taflan.util.Reference
import com.seadowg.taflan.util.reactive

class ItemItem : CardView, Reference {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val deleteClicks = EventStream<Unit>()

    private lateinit var table: Table.Existing
    private lateinit var item: Item.Existing
    private lateinit var tableRepository: TableRepository
    private lateinit var adapter: BaseAdapter

    private var initialized = false

    private fun initialize() {
        if (initialized) return

        val menuButton = findViewById<View>(R.id.menu)
        val popup = PopupMenu(context, menuButton)
        popup.inflate(R.menu.item_menu)

        menuButton.reactive().clicks.bind(this) {
            popup.show()
        }

        popup.setOnMenuItemClickListener {
            deleteClicks.occur(Unit)
            true
        }

        initialized = true
    }

    fun setItem(item: Item.Existing, table: Table.Existing, tableRepository: TableRepository, adapter: BaseAdapter): ItemItem {
        this.item = item
        this.table = table
        this.tableRepository = tableRepository
        this.adapter = adapter

        initialize()

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

    companion object {
        fun inflate(item: Item.Existing, table: Table.Existing, rootView: ViewGroup, context: Context, tableRepository: TableRepository, adapter: BaseAdapter): ItemItem {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_item, rootView, false) as ItemItem

            view.setItem(item, table, tableRepository, adapter)

            return view
        }
    }
}