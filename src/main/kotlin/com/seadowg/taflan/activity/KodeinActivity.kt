package com.seadowg.taflan.activity

import android.app.Activity
import android.os.Bundle
import com.github.salomonbrys.kodein.KodeinInjector
import com.seadowg.taflan.TaflanApplication

open class KodeinActivity : Activity() {

    protected val injector = KodeinInjector()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject((application as TaflanApplication).kodein)
    }
}