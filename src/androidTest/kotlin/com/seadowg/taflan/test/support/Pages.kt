package com.seadowg.taflan.test.support

import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers.*
import android.widget.Switch
import android.widget.ToggleButton
import com.seadowg.taflan.R
import io.pivotal.macchiato.pages.Page
import org.hamcrest.Matchers.allOf

class TablesPage : Page(withText("Taflan")) {

    fun clickFAB(): AddTablePage {
        onView(withId(R.id.fab)).perform(click())
        return assertOnPage(AddTablePage())
    }

    fun createTableFlow(name: String, items: List<String> = emptyList()): TablesPage {
        val tablesPage = clickFAB().fillInName(name).clickAdd()
        val tablePage = tablesPage.clickOnTableItem(name)

        items.forEach { item ->
            tablePage.clickFAB().fillInField("Name", item).clickAdd()
        }

        return tablePage.pressBack()
    }

    fun clickOnTableItem(name: String): TablePage {
        onView(withText(name)).perform(click())
        return assertOnPage(TablePage(name))
    }

    fun openMenu(): TablesPage.MenuPage {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        return MenuPage()
    }

    class MenuPage {

    }
}

class TablePage(val name: String) : Page(allOf(isDescendantOfA(withId(R.id.toolbar)), withText(name))) {

    fun clickFAB(): AddItemPage {
        onView(withId(R.id.fab)).perform(click())
        return AddItemPage(name)
    }

    fun editItem(itemName: String): EditItemPage {
        onView(itemCard(itemName)).perform(click())
        return assertOnPage(EditItemPage(tableName = name, itemName = itemName))
    }

    fun pressBack(): TablesPage {
        Espresso.pressBack()
        return assertOnPage(TablesPage())
    }

    fun deleteItem(itemName: String): TablePage {
        onView(allOf(isDescendantOfA(itemCard(itemName)), withContentDescription("menu"))).perform(click())
        onView(withText("Delete")).perform(click())
        return this
    }

    fun openMenu(): MenuPage {
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getTargetContext())
        return MenuPage(name)
    }

    private fun itemCard(itemName: String) = allOf(
            withContentDescription("item"),
            hasDescendant(allOf(hasSibling(withText("Name")), withText(itemName)))
    )

    class MenuPage(private val tableName: String) {
        fun clickAddField(): AddFieldPage {
            onView(withText("Add Field")).perform(click())
            return assertOnPage(AddFieldPage(tableName))
        }

        fun clickEdit(): EditTablePage {
            onView(withText("Edit")).perform(click())
            return EditTablePage(tableName)
        }
    }
}

class EditTablePage(tableName: String) : Page(allOf(withHint("Name"), withText(tableName))) {

    private var newTableName: String = tableName

    fun fillInName(name: String): EditTablePage {
        newTableName = name
        onView(withHint("Name")).perform(replaceText(name))
        return this
    }

    fun clickUpdate(): TablePage {
        onView(withText("Update")).perform(click())
        return assertOnPage(TablePage(newTableName))
    }

    fun clickDelete(): TablesPage {
        onView(withText("Delete")).perform(click())
        return assertOnPage(TablesPage())
    }
}

class EditItemPage(val tableName: String, itemName: String) : Page(allOf(isDescendantOfA(withId(R.id.toolbar)), withText(itemName))) {

    fun fillInField(name: String, with: String): EditItemPage {
        onView(withHint(name)).perform(replaceText(with))
        return this
    }

    fun clickUpdate(): TablePage {
        onView(withText("Update")).perform(click())
        return assertOnPage(TablePage(tableName))
    }

}

class AddFieldPage(private val tableName: String) : Page(allOf(isDescendantOfA(withId(R.id.toolbar)), withText("Add Field"))) {

    fun fillInName(name: String): AddFieldPage {
        onView(withHint("Name")).perform(replaceText(name))
        return this
    }

    fun clickAdd(): TablePage {
        onView(withText("Add")).perform(click())
        return assertOnPage(TablePage(tableName))
    }
}

class AddItemPage(private val tableName: String) : Page(allOf(isDescendantOfA(withId(R.id.toolbar)), withText("Add Item"))) {

    fun fillInField(name: String, with: String): AddItemPage {
        onView(withHint(name)).perform(replaceText(with), ViewActions.closeSoftKeyboard())
        return this
    }

    fun clickAdd(): TablePage {
        onView(withText("Add")).perform(click())
        return assertOnPage(TablePage(tableName))
    }

}

class AddTablePage : Page(withText("Add Table")) {

    fun fillInName(name: String): AddTablePage {
        onView(withHint("Name")).perform(replaceText(name))
        return this
    }

    fun clickAdd(): TablesPage {
        onView(withText("Add")).perform(click())
        return assertOnPage(TablesPage())
    }

}
