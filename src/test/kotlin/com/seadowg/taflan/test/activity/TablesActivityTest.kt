package com.seadowg.taflan.test.activity

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.TablesActivity
import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.tracking.Tracker
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.setupActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class TablesActivityTest {

    private val tracker: Tracker = mock()
    private val tableRepository: TableRepository = InMemoryTableRepository()

    @Before
    fun setup() {
        (RuntimeEnvironment.application as TaflanApplication).kodein = Kodein {
            bind<Tracker>().with(singleton { tracker })
            bind<ReactiveTableRepository>().with(singleton { ReactiveTableRepository(tableRepository) })
        }
    }

    @Test
    fun whenThereAreNoTables_tracksLoadTablesEvent() {
        tableRepository.clear()

        setupActivity(TablesActivity::class.java)
        verify(tracker).track("load_tables", value = 0)
    }

    @Test
    fun whenThereAreTables_tracksLoadTablesEvent() {
        TableCreator(tableRepository).create("A Table")
        TableCreator(tableRepository).create("Another Table")

        setupActivity(TablesActivity::class.java)
        verify(tracker).track("load_tables", value = 2)
    }
}