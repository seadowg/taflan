package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import com.seadowg.taflan.activity.LaunchActivity
import android.support.test.rule.ActivityTestRule
import android.view.KeyEvent
import org.junit.Rule
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.test.support.TablePage
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.Matchers.not
import org.junit.Test


@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateTableTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canAddATable() {
        val tablesPage = TablesPage()

        val addTablePage = tablesPage.clickFAB()
        addTablePage.fillInName("Shopping list").clickAdd()

        onView(withText("Shopping list")).check(matches(isDisplayed()))
    }

    @Test
    fun cannotCreateTableWithEmptyName() {
        val tablesPage = TablesPage()

        val addTablePage = tablesPage.clickFAB()
        addTablePage.fillInName("")

        onView(withText("Add")).check(matches(not(isEnabled())))
    }

    @Test
    fun cannotCreateATableWithMultilineName() {
        val tablesPage = TablesPage()

        val addTablePage = tablesPage.clickFAB()
        onView(withHint("Name")).perform(typeText("thing"), pressKey(KeyEvent.KEYCODE_ENTER), typeText("other thing"))

        addTablePage.clickAdd()
        onView(withText("thingother thing")).check(matches(isDisplayed()))
    }
}
