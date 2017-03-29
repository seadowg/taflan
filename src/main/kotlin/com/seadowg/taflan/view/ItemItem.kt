package com.seadowg.taflan.view

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item

class ItemItem : CardView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(item: Item, rootView: ViewGroup, context: Context): ItemItem {
            val view = LayoutInflater.from(context).inflate(R.layout.item_item, rootView, false) as ItemItem
            val name = view.findViewById(R.id.name) as TextView
            name.text = item.value

            return view
        }
    }
}