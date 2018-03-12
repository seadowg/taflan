package com.seadowg.taflan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table

class TableItem : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(table: Table, rootView: ViewGroup, context: Context): TableItem {
            val view = LayoutInflater.from(context).inflate(R.layout.table_item, rootView, false)
            val name = view.findViewById<TextView>(R.id.name)
            name.text = table.name

            view.findViewById<View>(R.id.content).background = table.colorDrawable(context)

            return view as TableItem
        }
    }
}
