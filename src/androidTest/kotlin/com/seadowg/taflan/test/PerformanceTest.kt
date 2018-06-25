package com.seadowg.taflan.test

import androidx.test.runner.AndroidJUnit4
import com.seadowg.taflan.test.support.TablesPage
import com.seadowg.taflan.test.support.TaflanEspressoTestRule
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@Ignore("Only run sometimes")
class PerformanceTest {

    @get:Rule
    val mActivityRule = TaflanEspressoTestRule()

    @Test
    fun canLoad10TablesWith100Items() {
        (1..10).forEach { table ->
            TablesPage()
                    .createTableFlow("Table $table", (1..100).map { item -> "Item $item" })
                    .clickOnTableItem("Table $table")
                    .pressBack()
        }
    }
}