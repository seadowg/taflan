package com.seadowg.taflan.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
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
