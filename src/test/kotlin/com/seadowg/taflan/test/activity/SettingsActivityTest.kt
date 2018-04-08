package com.seadowg.taflan.test.activity

import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.SettingsActivity
import com.seadowg.taflan.tracking.Tracker
import kotlinx.android.synthetic.main.settings.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.setupActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class SettingsActivityTest {

    private val tracker: Tracker = mock()

    @Before
    fun setup() {
        (RuntimeEnvironment.application as TaflanApplication).kodein = Kodein {
            bind<Tracker>().with(singleton { tracker })
        }
    }

    @Test
    fun loadsAnalyticsEnabledValue() {
        PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)
                .edit()
                .putBoolean("analytics_enabled", true)
                .apply()

        var activity = setupActivity(SettingsActivity::class.java)
        assertThat(activity.analytics_enabled   .isChecked).isTrue()

        PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)
                .edit()
                .putBoolean("analytics_enabled", false)
                .apply()

        activity = setupActivity(SettingsActivity::class.java)
        assertThat(activity.analytics_enabled.isChecked).isFalse()
    }

    @Test
    fun togglingAnalytics_disablesAndEnablesTracker() {
        val activity = setupActivity(SettingsActivity::class.java)

        activity.analytics_enabled.isChecked = false
        verify(tracker).isEnabled = false

        activity.analytics_enabled.isChecked = true
        verify(tracker).isEnabled = true
    }

    @Test
    fun togglingAnalytics_storesPreference() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)
        val activity = setupActivity(SettingsActivity::class.java)

        activity.analytics_enabled.isChecked = false
        assertThat(preferences.getBoolean("analytics_enabled", true)).isFalse()

        activity.analytics_enabled.isChecked = true
        assertThat(preferences.getBoolean("analytics_enabled", false)).isTrue()
    }
}