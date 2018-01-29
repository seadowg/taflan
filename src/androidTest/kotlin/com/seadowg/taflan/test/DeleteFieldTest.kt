package com.seadowg.taflan.test

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.doesNotExist
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.filters.LargeTest
import android.support.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
@Ignore
class DeleteFieldTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canDeleteField() {
        val tablesPage = TablesPage().createTableFlow(
                "Shopping list",
                items = listOf("Bananas", "Eggs")
        )

        val shoppingListPage = tablesPage
                .clickOnTableItem("Shopping list")
                .clickFAB()
                .clickAddField()
                .fillInName("Quantity")
                .clickAdd()

        shoppingListPage
                .clickFAB()
                .clickFields()
                .deleteField("Quantity")
                .pressBack()

        onView(withText("Quantity")).check(doesNotExist())
    }
}
