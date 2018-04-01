package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.RootMatchers.withDecorView
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import android.support.v7.widget.SwitchCompat
import android.widget.Checkable
import android.widget.Switch
import android.widget.ToggleButton
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.containsString
import org.hamcrest.core.Is.`is`
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingsTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canDisableTrackingAndCrashReporting() {
        val tablesPage = TablesPage()
        val settingsPage = tablesPage.openMenu().clickSettings()

        settingsPage.toggleTracking()
        onView(allOf(hasSibling(withText(containsString("Analytics and crash reporting"))), isAssignableFrom(Switch::class.java)))
                .check(matches(isNotChecked()))
    }
}