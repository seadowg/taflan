package com.seadowg.taflan.test.support

import android.graphics.drawable.ColorDrawable
import android.view.View

fun View.backgroundColor(): Int {
    return (background as ColorDrawable).color
}
