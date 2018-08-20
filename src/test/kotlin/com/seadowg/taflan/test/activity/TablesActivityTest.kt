package com.seadowg.taflan.test.activity

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.TablesActivity
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.usecase.TableCreator
import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.tracking.Tracker
import kotlinx.android.synthetic.main.launch.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.setupActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.fakes.RoboMenuItem

@RunWith(RobolectricTestRunner::class)
class TablesActivityTest {

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

    @Test
    fun whenThereAreTables_clickingOnTable_tracksLoadItemsEvent() {
        val table1 = TableCreator(tableRepository).create("A Table")
        val table2 = TableCreator(tableRepository).create("Another Table")
        tableRepository.addItem(table2.id, Item.New(listOf("blah")))
        tableRepository.addItem(table2.id, Item.New(listOf("blah1")))

        val activity = setupActivity(TablesActivity::class.java)

        activity.tables.getChildAt(0).performClick()
        verify(tracker).track("load_items", value = 0)

        activity.tables.getChildAt(1).performClick()
        verify(tracker).track("load_items", value = 2)
    }

    @Test
    fun clickingOpenSourceLicensesInMenu_opensOpenSourceLicenseActivity() {
        val activity = setupActivity(TablesActivity::class.java)
        activity.onOptionsItemSelected(RoboMenuItem(R.id.open_source_licenses))

        val nextStartedActivity = shadowOf(activity).nextStartedActivity
        assertThat(nextStartedActivity.component.className).isEqualTo(OssLicensesMenuActivity::class.java.name)
    }
}