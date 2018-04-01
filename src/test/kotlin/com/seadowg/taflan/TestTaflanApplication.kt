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

class TestTaflanApplication : TaflanApplication() {

    override fun onCreate() {
        super.onCreate()
        setupKodein(this, tracking = false)
    }
}
