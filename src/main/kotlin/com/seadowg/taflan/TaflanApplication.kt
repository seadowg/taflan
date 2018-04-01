package com.seadowg.taflan

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.google.firebase.analytics.FirebaseAnalytics
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.SharedPreferencesTableRepository
import com.seadowg.taflan.tracking.FirebaseTracker
import com.seadowg.taflan.tracking.Tracker
import com.seadowg.taflan.util.AndroidContentReader
import com.seadowg.taflan.util.ContentReader
import com.squareup.leakcanary.LeakCanary

open class TaflanApplication : Application() {

    lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()

        if (setupLeakCanary()) return

        setupKodein(this)
    }

    private fun setupLeakCanary(): Boolean {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            true
        } else {
            LeakCanary.install(this)
            false
        }
    }

    fun setupKodein(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val tableRepository = SharedPreferencesTableRepository(preferences)

        val tracker = createTracker(context)

        kodein = Kodein {
            bind<ContentReader>().with(singleton { AndroidContentReader(applicationContext) })
            bind<ReactiveTableRepository>().with(singleton { ReactiveTableRepository(tableRepository) })
            bind<Tracker>().with(singleton { tracker })
        }
    }

    private fun createTracker(context: Context): Tracker {
        val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        return FirebaseTracker(firebaseAnalytics)
    }
}
