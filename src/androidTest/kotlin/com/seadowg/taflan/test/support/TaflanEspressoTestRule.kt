package com.seadowg.taflan.test.support

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.TablesActivity
import com.seadowg.taflan.activity.TaflanActivity
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.tracking.Tracker

class TaflanEspressoTestRule : ActivityTestRule<TablesActivity>(TablesActivity::class.java) {

    private val injector = KodeinInjector()
    private val tableRepository: ReactiveTableRepository by injector.instance()
    private val tracker: Tracker by injector.instance()

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val context = InstrumentationRegistry.getTargetContext().applicationContext
        val application = context as TaflanApplication

        application.setupKodein(context)
        injector.inject(application.kodein)

        tableRepository.store.clear()
        tracker.isEnabled = false
        TaflanActivity.TEST_MODE = true
    }

}
