package com.seadowg.taflan.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import java.util.*

class TableItem : android.support.v7.widget.CardView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    companion object {
        fun inflate(table: Table, rootView: ViewGroup, context: Context): TableItem {
            val view = LayoutInflater.from(context).inflate(R.layout.table_item, rootView, false)
            val name = view.findViewById(R.id.name) as TextView
            name.text = table.name

            view.findViewById(R.id.content).background = backgroundForTable(table, context)

            return view as TableItem
        }

        private fun backgroundForTable(table: Table, context: Context): Drawable {
            val resource = when(table.color) {
                is Color.Red -> R.color.card_red
                is Color.Blue -> R.color.card_blue
                is Color.Green -> R.color.card_green
                is Color.Orange -> R.color.card_orange
            }

            return ColorDrawable(context.resources.getColor(resource))
        }
    }
}
