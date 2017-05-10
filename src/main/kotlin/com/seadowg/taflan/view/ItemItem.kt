package com.seadowg.taflan.view

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.PopupMenu
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.util.EventStream
import com.seadowg.taflan.util.reactive

class ItemItem : CardView, PopupMenu.OnMenuItemClickListener {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    val clicks: EventStream<Unit>
        get() { return reactive().clicks }

    val deleteClicks: EventStream<Unit> = EventStream()

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        deleteClicks.occur(Unit)
        return true
    }

    fun setItem(item: Item.Existing, table: Table.Existing): ItemItem {
        val fieldsList = findViewById(R.id.fields) as ViewGroup
        fieldsList.removeAllViews()

        item.values.forEachIndexed { index, value ->
            val itemField = LayoutInflater.from(context).inflate(R.layout.item_field, fieldsList, false)

            val nameView = itemField.findViewById(R.id.name) as TextView
            val valueView = itemField.findViewById(R.id.value) as TextView

            nameView.text = table.fields[index]
            valueView.text = value
            fieldsList.addView(itemField)
        }

        val menuButton = findViewById(R.id.menu)
        val popup = PopupMenu(context, menuButton)
        popup.inflate(R.menu.item_menu)
        popup.setOnMenuItemClickListener(this)

        menuButton.reactive().clicks.unbind(this)
        menuButton.reactive().clicks.bind(this) {
            popup.show()
        }

        return this
    }

    companion object {
        fun inflate(item: Item.Existing, table: Table.Existing, rootView: ViewGroup, context: Context): ItemItem {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_item, rootView, false) as ItemItem

            view.setItem(item, table)

            return view
        }
    }
}