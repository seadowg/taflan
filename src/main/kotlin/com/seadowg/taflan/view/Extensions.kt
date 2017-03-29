package com.seadowg.taflan.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table

fun Table.colorDrawable(context: Context): Drawable {
    val resource = when(this.color) {
        is Color.Red -> R.color.card_red
        is Color.Blue -> R.color.card_blue
        is Color.Green -> R.color.card_green
        is Color.Orange -> R.color.card_orange
    }

    return ColorDrawable(context.resources.getColor(resource))
}
