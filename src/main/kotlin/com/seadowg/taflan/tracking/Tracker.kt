package com.seadowg.taflan.tracking

import android.content.Context


interface Tracker {
    var isEnabled: Boolean
    fun track(event: String, parameters: Map<String, String>? = null, value: Long? = null)
}

class FirebaseTracker(context: Context, enabled: Boolean) : Tracker {

//    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override var isEnabled: Boolean = true
        set(value) {
//            firebaseAnalytics.setAnalyticsCollectionEnabled(value)
            field = value
        }

    init {
//        if (enabled) {
//            Fabric.with(context, Crashlytics())
//        }

        isEnabled = enabled
    }

    override fun track(event: String, parameters: Map<String, String>?, value: Long?) {
//        val bundle = parameters?.toList()?.fold(Bundle()) { bundle, (key, value) ->
//            bundle.apply { putString(key, value) }
//        } ?: Bundle()
//
//        value?.let { bundle.putLong(FirebaseAnalytics.Param.VALUE, value) }
//
//        firebaseAnalytics.logEvent(event, bundle)
    }
}