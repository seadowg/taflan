package com.seadowg.taflan.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import androidx.appcompat.widget.SwitchCompat
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

        onView(allOf(hasSibling(withText(containsString("Analytics and crash reporting"))), isAssignableFrom(Switch::class.java)))
                .check(matches(isChecked()))

        settingsPage.toggleTracking()
        onView(allOf(hasSibling(withText(containsString("Analytics and crash reporting"))), isAssignableFrom(Switch::class.java)))
                .check(matches(isNotChecked()))
    }
}