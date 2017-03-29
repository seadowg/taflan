package com.seadowg.taflan.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table

class ItemItem : CardView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(item: Item, table: Table, rootView: ViewGroup, context: Context): ItemItem {
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.item_item, rootView, false) as ItemItem

            val fieldsList = view.findViewById(R.id.fields) as ViewGroup
            item.values.forEachIndexed { index, value ->
                val itemField = inflater.inflate(R.layout.item_field, fieldsList, false)

                val nameView = itemField.findViewById(R.id.name) as TextView
                val valueView = itemField.findViewById(R.id.value) as TextView

                nameView.text = table.fields[index]
                valueView.text = value
                fieldsList.addView(itemField)
            }

            return view
        }
    }
}