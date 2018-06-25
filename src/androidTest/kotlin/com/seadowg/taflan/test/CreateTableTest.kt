package com.seadowg.taflan.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import android.view.KeyEvent
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
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
