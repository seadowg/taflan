package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablePage
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.hamcrest.Matchers
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddFieldsTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canAddFieldsToATable() {
        val tablesPage = TablesPage().createTableFlow("Shopping list")
        var shoppingListPage = tablesPage.clickOnTableItem("Shopping list")

        var addFieldPage = shoppingListPage.clickAddField()
        shoppingListPage = addFieldPage.fillInName("Quantity").clickAdd()

        var addItemPage = shoppingListPage.clickAddItem()
        addItemPage.fillInField("Name", "Bananas")
        addItemPage.fillInField("Quantity", "5")

        shoppingListPage = addItemPage.clickAdd()
        onView(allOf(hasSibling(withText("Name")), withText("Bananas"))).check(matches(isDisplayed()))
        onView(allOf(hasSibling(withText("Quantity")), withText("5"))).check(matches(isDisplayed()))

        addFieldPage = shoppingListPage.clickAddField()
        shoppingListPage = addFieldPage.fillInName("Healthy?").clickAdd()

        addItemPage = shoppingListPage.clickAddItem()
        addItemPage.fillInField("Name", "Cheese")
        addItemPage.fillInField("Quantity", "1")
        addItemPage.fillInField("Healthy?", "No...")

        shoppingListPage = addItemPage.clickAdd()
        onView(allOf(hasSibling(withText("Name")), withText("Cheese"))).check(matches(isDisplayed()))
        onView(allOf(hasSibling(withText("Quantity")), withText("1"))).check(matches(isDisplayed()))
        onView(allOf(hasSibling(withText("Healthy?")), withText("No..."))).check(matches(isDisplayed()))
    }

    @Test
    fun cannotAddAFieldWithAnEmptyName() {
        val tablesPage = TablesPage().createTableFlow("Shopping list")
        var shoppingListPage = tablesPage.clickOnTableItem("Shopping list")

        var addFieldPage = shoppingListPage.clickAddField()
        addFieldPage.fillInName("")

        onView(withText("Add")).check(matches(Matchers.not(isEnabled())))
    }
}