package com.seadowg.taflan.test.support

import android.support.test.InstrumentationRegistry
import android.support.test.rule.ActivityTestRule
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.LaunchActivity

class TaflanEspressoTestRule : ActivityTestRule<LaunchActivity>(LaunchActivity::class.java) {

    override fun beforeActivityLaunched() {
        super.beforeActivityLaunched()

        val context = InstrumentationRegistry.getTargetContext().applicationContext
        val application = context as TaflanApplication
        application.setupTest()
    }
}
