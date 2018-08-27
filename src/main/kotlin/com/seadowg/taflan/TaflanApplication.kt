package com.seadowg.taflan

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import android.provider.Settings
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
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
        setupKodein()
    }

    fun setupKodein() {
        kodein = dependencies(this)
    }

    protected open fun dependencies(context: Context): Kodein {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val tableRepository = SharedPreferencesTableRepository(preferences)

        return Kodein {
            bind<ContentReader>().with(singleton { AndroidContentReader(applicationContext) })
            bind<TableRepository>().with(singleton { tableRepository })
            bind<Tracker>().with(singleton { createTracker(context) })
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
