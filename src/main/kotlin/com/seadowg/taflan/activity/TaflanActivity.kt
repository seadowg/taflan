package com.seadowg.taflan.activity

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.github.salomonbrys.kodein.KodeinInjector
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication

open class TaflanActivity : AppCompatActivity() {

    protected val injector = KodeinInjector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject((application as TaflanApplication).kodein)
    }

    protected fun setupToolbar(title: String, color: Drawable? = null, backArrow: Boolean = false) {
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = title

        if (backArrow) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)

            val upArrow = resources.getDrawable(R.drawable.abc_ic_ab_back_material)
            upArrow.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_ATOP)
            supportActionBar?.setHomeAsUpIndicator(upArrow)

        }

        if (color != null) {
            supportActionBar?.setBackgroundDrawable(color)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        var TEST_MODE = false
    }
}