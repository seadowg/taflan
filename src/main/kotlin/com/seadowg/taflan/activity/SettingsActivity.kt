package com.seadowg.taflan.activity

import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.Toast
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
        analytics_enabled.isChecked = preferences.getBoolean("analytics_enabled", true)

        analytics_enabled.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Requires restart to take effect!", Toast.LENGTH_SHORT).show()

            tracker.isEnabled = isChecked
            preferences
                    .edit()
                    .putBoolean("analytics_enabled", isChecked)
                    .apply()
        }
    }
}