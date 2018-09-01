package com.seadowg.taflan.test.support

import android.preference.PreferenceManager
import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.MainActivity
import com.seadowg.taflan.tracking.Tracker

class TaflanEspressoTestRule : ActivityTestRule<MainActivity>(MainActivity::class.java) {

    private val injector = KodeinInjector()
    private val tracker: Tracker by injector.instance()

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val context = InstrumentationRegistry.getTargetContext().applicationContext
        val application = context as TaflanApplication

        application.setupKodein()
        injector.inject(application.kodein)

        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply()
        tracker.isEnabled = false
    }

}
