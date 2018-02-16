package com.seadowg.taflan.test.support

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.LaunchActivity
import com.seadowg.taflan.activity.TaflanActivity
import com.seadowg.taflan.repository.ReactiveTableRepository

class TaflanEspressoTestRule : ActivityTestRule<LaunchActivity>(LaunchActivity::class.java) {

    private val injector = KodeinInjector()

    private val tableRepository: ReactiveTableRepository by injector.instance()

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()
        val context = InstrumentationRegistry.getTargetContext().applicationContext
        val application = context as TaflanApplication

        application.setupKodein()
        injector.inject(application.kodein)

        tableRepository.store.clear()
        TaflanActivity.TEST_MODE = true
    }

}
