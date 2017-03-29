package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import com.seadowg.taflan.activity.LaunchActivity
import android.support.test.rule.ActivityTestRule
import org.junit.Rule
import com.seadowg.taflan.R
import org.hamcrest.Matchers.not
import org.junit.Test


@RunWith(AndroidJUnit4::class)
@LargeTest
class CreateTableTest {

    @get:Rule
    val mActivityRule = ActivityTestRule(
            LaunchActivity::class.java)

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
}

class TablesPage {

    init {
        onView(withText("Taflan")).check(matches(isDisplayed()))
    }

    fun clickFAB(): AddTablePage {
        onView(withId(R.id.fab)).perform(click())
        return AddTablePage()
    }
}

class AddTablePage {

    init {
        onView(withText("Add Table")).check(matches(isDisplayed()))
    }

    fun fillInName(name: String): AddTablePage {
        onView(withHint("Name")).perform(typeText(name))
        return this
    }

    fun clickAdd(): TablesPage {
        onView(withText("Add")).perform(click())
        return TablesPage()
    }

}
