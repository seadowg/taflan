package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DeleteTableTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canDeleteTable() {
        val tablesPage = TablesPage().createTableFlow(
                "Shopping list",
                items = listOf("Bananas")
        )

        val shoppingListPage = tablesPage.clickOnTableItem("Shopping list")
        shoppingListPage.openMenu()
                .clickEdit()
                .clickDelete()

        onView(withText("Shopping list")).check(doesNotExist())
    }
}
