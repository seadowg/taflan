package com.seadowg.taflan.view

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table

fun Table.colorDrawable(context: Context): ColorDrawable {
    val resource = when(this.color) {
        Color.Red -> R.color.card_red
        Color.Blue -> R.color.card_blue
        Color.Green -> R.color.card_green
        Color.Orange -> R.color.card_orange
    }

    return ColorDrawable(context.resources.getColor(resource))
}
