package com.seadowg.taflan.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablePage
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ExportTableTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canExportATable() {
        val tablesPage = TablesPage()
                .createTableFlow("Shopping list", items = listOf("Bananas", "Eggs"))

        tablesPage.clickOnTableItem("Shopping list").openMenu()
        onView(withText("Export")).check(matches(isDisplayed()))
    }
}
