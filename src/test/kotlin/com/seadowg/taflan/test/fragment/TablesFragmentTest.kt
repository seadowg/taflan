package com.seadowg.taflan.test.fragment

import android.view.ViewGroup
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.fragment.TablesFragment
import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.tracking.Tracker
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.shadows.support.v4.SupportFragmentController

@RunWith(RobolectricTestRunner::class)
class TablesFragmentTest {

    private val tracker: Tracker = mock()
    private val tableRepository: TableRepository = InMemoryTableRepository()

    @Before
    fun setup() {
        (RuntimeEnvironment.application as TaflanApplication).kodein = Kodein {
            bind<Tracker>().with(singleton { tracker })
            bind<TableRepository>().with(singleton { tableRepository })
        }
    }

    @Test
    fun whenThereAreNoTables_tracksLoadTablesEvent() {
        tableRepository.clear()

        SupportFragmentController.setupFragment(TablesFragment())
        verify(tracker).track("load_tables", value = 0)
    }

    @Test
    fun whenThereAreTables_tracksLoadTablesEvent() {
        TableCreator(tableRepository).create("A Table")
        TableCreator(tableRepository).create("Another Table")

        SupportFragmentController.setupFragment(TablesFragment())
        verify(tracker).track("load_tables", value = 2)
    }

    @Test
    fun whenThereAreTables_clickingOnTable_tracksLoadItemsEvent() {
        val table1 = TableCreator(tableRepository).create("A Table")
        val table2 = TableCreator(tableRepository).create("Another Table")
        tableRepository.addItem(table2.id, Item.New(listOf("blah")))
        tableRepository.addItem(table2.id, Item.New(listOf("blah1")))

        val fragment = SupportFragmentController.setupFragment(TablesFragment())

        fragment.view!!.findViewById<ViewGroup>(R.id.tables).getChildAt(0).performClick()
        verify(tracker).track("load_items", value = 0)

        fragment.view!!.findViewById<ViewGroup>(R.id.tables).getChildAt(1).performClick()
        verify(tracker).track("load_items", value = 2)
    }
}