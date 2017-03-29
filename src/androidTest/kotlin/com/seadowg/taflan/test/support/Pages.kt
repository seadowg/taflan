package com.seadowg.taflan.test.support

import android.support.test.espresso.Espresso
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import com.seadowg.taflan.R
import org.hamcrest.Matchers.allOf

class TablesPage {

    init {
        onView(withText("Taflan")).check(matches(isDisplayed()))
    }

    fun clickFAB(): AddTablePage {
        onView(withId(R.id.fab)).perform(click())
        return AddTablePage()
    }

    fun createTableFlow(name: String): TablesPage {
        return clickFAB().fillInName(name).clickAdd()
    }

    fun clickOnTableItem(name: String): TablePage {
        onView(withText(name)).perform(click())
        return TablePage(name)
    }
}

class TablePage(val name: String) {

    init {
        onView(allOf(isDescendantOfA(withId(R.id.toolbar)), withText(name))).check(matches(isDisplayed()))
    }

    fun clickAddItem(): AddItemPage {
        onView(withId(R.id.fab_helper)).perform(click())
        onView(withText("Add Item")).perform(click())
        return AddItemPage(name)
    }

    fun clickAddField(): AddFieldPage {
        onView(withId(R.id.fab_helper)).perform(click())
        onView(withText("Add Field")).perform(click())
        return AddFieldPage(name)
    }
}

class AddFieldPage(val tableName: String) {

    init {
        onView(allOf(isDescendantOfA(withId(R.id.toolbar)), withText("Add Field"))).check(matches(isDisplayed()))
    }

    fun fillInName(name: String): AddFieldPage {
        onView(withHint("Name")).perform(typeText(name))
        return this
    }

    fun clickAdd(): TablePage {
        onView(withText("Add")).perform(click())
        return TablePage(tableName)
    }
}

class AddItemPage(val tableName: String) {

    init {
        onView(allOf(isDescendantOfA(withId(R.id.toolbar)), withText("Add Item"))).check(matches(isDisplayed()))
    }

    fun fillInField(name: String, with: String): AddItemPage {
        onView(withHint(name)).perform(typeText(with))
        return this
    }

    fun clickAdd(): TablePage {
        onView(withText("Add")).perform(click())
        return TablePage(tableName)
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
