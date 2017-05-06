package com.seadowg.taflan.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import java.util.*

class TableItem : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(table: Table, rootView: ViewGroup, context: Context): TableItem {
            val view = LayoutInflater.from(context).inflate(R.layout.table_item, rootView, false)
            val name = view.findViewById(R.id.name) as TextView
            name.text = table.name

            view.findViewById(R.id.content).background = table.colorDrawable(context)

            return view as TableItem
        }
    }
}
