package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddItemsToTableTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canAddItemsToTable() {
        val tablesPage = TablesPage().createTableFlow("Shopping list")

        var shoppingListPage = tablesPage.clickOnTableItem("Shopping list")

        shoppingListPage = shoppingListPage
                .clickFAB()
                .fillInField("Name", with = "Bananas")
                .clickAdd()

        onView(allOf(hasSibling(withText("Name")), withText("Bananas"))).check(matches(isDisplayed()))

        shoppingListPage = shoppingListPage
                .clickFAB()
                .fillInField("Name", with = "Salami")
                .clickAdd()


        onView(allOf(hasSibling(withText("Name")), withText("Salami"))).check(matches(isDisplayed()))
    }
}
