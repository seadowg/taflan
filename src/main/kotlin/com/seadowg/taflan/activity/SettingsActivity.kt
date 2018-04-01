package com.seadowg.taflan.activity

import android.os.Bundle
import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.tracking.Tracker
import kotlinx.android.synthetic.main.settings.*

class SettingsActivity : TaflanActivity() {

    private val tracker: Tracker by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)
        setupToolbar(getString(R.string.settings), backArrow = true)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        analytics.isChecked = preferences.getBoolean("analytics_enabled", true)

        analytics.setOnCheckedChangeListener { _, isChecked ->
            tracker.isEnabled = isChecked
            preferences
                    .edit()
                    .putBoolean("analytics_enabled", isChecked)
                    .apply()
        }
    }
}