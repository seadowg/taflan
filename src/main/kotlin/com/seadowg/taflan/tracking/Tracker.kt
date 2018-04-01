package com.seadowg.taflan.tracking

import android.os.Bundle
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics

interface Tracker {
    var isEnabled: Boolean
    fun track(event: String, parameters: Map<String, String>? = null, value: Long? = null)
}

class FirebaseTracker(private val firebaseAnalytics: FirebaseAnalytics) : Tracker {
    override var isEnabled: Boolean = true
        set(value) {
            firebaseAnalytics.setAnalyticsCollectionEnabled(value)
            field = value
        }

    override fun track(event: String, parameters: Map<String, String>?, value: Long?) {
        val bundle = parameters?.toList()?.fold(Bundle()) { bundle, (key, value) ->
            bundle.apply { putString(key, value) }
        } ?: Bundle()

        value?.let { bundle.putLong(FirebaseAnalytics.Param.VALUE, value) }

        firebaseAnalytics.logEvent(event, bundle)
    }
}