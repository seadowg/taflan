package com.seadowg.taflan.test.activity

import android.content.Intent
import android.view.View
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.BuildConfig
import com.seadowg.taflan.R
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.TableActivity
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.sample
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class TableActivityTest {

    private val injector = KodeinInjector()
    private val reactiveTableRepository: ReactiveTableRepository by injector.instance()
    private val tableRepository: TableRepository by lazy { reactiveTableRepository.store }

    @Before
    fun setup() {
        injector.inject((RuntimeEnvironment.application as TaflanApplication).kodein)
    }

    @Test
    fun clickingExport_sendsCSVShareIntent() {
        var table = tableRepository.create(Table.New("Shopping list", Color.values().sample(), listOf("Name", "Quantity")))
        table = tableRepository.addItem(table, Item.New(listOf("Banana\nThe good ones", "5")))
        table = tableRepository.addItem(table, Item.New(listOf("Kiwi, because they are my favourite", "2")))

        val tableIntent = TableActivity.intent(RuntimeEnvironment.application, table)
        val activity = buildActivity(TableActivity::class.java, tableIntent).setup().get()

        activity.findViewById<View>(R.id.export).performClick()

        val chooserIntent = Shadows.shadowOf(activity).nextStartedActivity
        assertThat(chooserIntent.action).isEqualTo(Intent.ACTION_CHOOSER)

        val sendIntent = chooserIntent.extras[(Intent.EXTRA_INTENT)] as Intent
        assertThat(sendIntent.action).isEqualTo(Intent.ACTION_SEND)
        assertThat(sendIntent.type).isEqualTo("text/csv")
        assertThat(chooserIntent.extras[Intent.EXTRA_TITLE]).isEqualTo("Export \"Shopping list\" as .csv")
        assertThat(sendIntent.extras[Intent.EXTRA_TEXT]).isEqualTo("\"Name\",\"Quantity\"\n\"Banana\nThe good ones\",\"5\"\n\"Kiwi, because they are my favourite\",\"2\"\n")
    }
}
