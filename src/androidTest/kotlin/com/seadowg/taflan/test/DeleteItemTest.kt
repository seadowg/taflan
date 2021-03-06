package com.seadowg.taflan.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.Matchers.allOf
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

    @Test
    fun deletingAnItem_thenAddingAnother_doesntCauseOriginalItemToReappear() {
        val tablesPage = TablesPage().createTableFlow(
                "Shopping list",
                items = listOf("Bananas", "Eggs")
        )

        val shoppingListPage = tablesPage.clickOnTableItem("Shopping list")
        shoppingListPage
                .deleteItem("Eggs")
                .clickFAB()
                .fillInField("Name", "Bacon")
                .clickAdd()

        onView(allOf(hasSibling(withText("Name")), withText("Eggs"))).check(doesNotExist())
        onView(allOf(hasSibling(withText("Name")), withText("Bananas"))).check(matches(isDisplayed()))
        onView(allOf(hasSibling(withText("Name")), withText("Bacon"))).check(matches(isDisplayed()))
    }
}