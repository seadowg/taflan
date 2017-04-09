package com.seadowg.taflan.test

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.matcher.ViewMatchers
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.Matchers
import org.hamcrest.Matchers.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class DeleteItemTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canDeleteItem() {
        val tablesPage = TablesPage().createTableFlow(
                "Shopping list",
                items = listOf("Bananas", "Eggs")
        )

        var shoppingListPage = tablesPage.clickOnTableItem("Shopping list")
        shoppingListPage = shoppingListPage.deleteItem("Eggs")

        onView(allOf(hasSibling(withText("Name")), withText("Eggs"))).check(doesNotExist())
        onView(allOf(hasSibling(withText("Name")), withText("Bananas"))).check(matches(isDisplayed()))
    }
}