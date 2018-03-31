package com.seadowg.taflan.view

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.os.Build
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

    return ColorDrawable(context.resources.getColorPolyfill(resource, context.theme))
}

@Suppress("DEPRECATION")
fun Resources.getColorPolyfill(resource: Int, theme: Resources.Theme): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(resource, theme)
    } else {
        getColor(resource)
    }
}
