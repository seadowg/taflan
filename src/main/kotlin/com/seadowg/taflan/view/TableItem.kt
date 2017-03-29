package com.seadowg.taflan.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table

class TableItem : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    companion object {
        fun inflate(table: Table, rootView: ViewGroup, context: Context): TableItem {
            val view = LayoutInflater.from(context).inflate(R.layout.table_item, rootView, false)
            val name = view.findViewById(R.id.name) as TextView
            name.text = table.name

            return view as TableItem
        }
    }
}
