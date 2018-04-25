package com.seadowg.taflan.activity

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import com.github.salomonbrys.kodein.KodeinInjector
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.util.EventStream
import com.seadowg.taflan.util.Navigator

abstract class TaflanActivity : AppCompatActivity() {

    protected val injector = KodeinInjector()

    private val reactives = mutableListOf<Pair<() -> Any, EventStream<Any>>>()

    protected val navigator = Navigator(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject((application as TaflanApplication).kodein)
    }

    override fun onResume() {
        super.onResume()

        reactives.forEach { it.second.occur(it.first()) }
    }

    protected fun setupToolbar(title: String, color: Drawable? = null, backArrow: Boolean = false) {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = title

        if (backArrow) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setDisplayShowHomeEnabled(true)
        }

        color?.let {
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