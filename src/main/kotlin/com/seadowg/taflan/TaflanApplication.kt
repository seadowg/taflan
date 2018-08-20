package com.seadowg.taflan

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.provider.Settings
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.google.firebase.analytics.FirebaseAnalytics
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.SharedPreferencesTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.tracking.FirebaseTracker
import com.seadowg.taflan.tracking.Tracker
import com.seadowg.taflan.util.AndroidContentReader
import com.seadowg.taflan.util.ContentReader

open class TaflanApplication : Application() {

    lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()
        setupKodein(this)
    }

    fun setupKodein(context: Context) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val tableRepository = SharedPreferencesTableRepository(preferences)

        val tracker = createTracker(context)

        kodein = Kodein {
            bind<ContentReader>().with(singleton { AndroidContentReader(applicationContext) })
            bind<TableRepository>().with(singleton { tableRepository })
            bind<ReactiveTableRepository>().with(singleton { ReactiveTableRepository(tableRepository) })
            bind<Tracker>().with(singleton { tracker })
        }
    }

    private fun createTracker(context: Context): Tracker {
        val testLabSetting = Settings.System.getString(context.contentResolver, "firebase.test.lab")
        if ("true" == testLabSetting) {
            return FirebaseTracker(context, false)
        }

        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val analyticsEnabled = preferences.getBoolean("analytics_enabled", true)
        return FirebaseTracker(context, analyticsEnabled)
    }
}
