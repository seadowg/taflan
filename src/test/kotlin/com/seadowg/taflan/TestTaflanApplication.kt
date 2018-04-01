package com.seadowg.taflan

import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.tracking.Tracker

class TestTaflanApplication : TaflanApplication() {

    private val injector = KodeinInjector()
    private val tracker: Tracker by injector.instance()

    override fun onCreate() {
        super.onCreate()
        injector.inject(this.kodein)

        tracker.isEnabled = false
    }
}
