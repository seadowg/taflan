package com.seadowg.taflan.test.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.EditText
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.TaflanApplication
import com.seadowg.taflan.activity.ImportCSVActivity
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.TableRepository
import kotlinx.android.synthetic.main.form.view.*
import kotlinx.android.synthetic.main.new_table.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.shadows.ShadowContentResolver
import java.io.File
import java.io.StringReader

@RunWith(RobolectricTestRunner::class)
class ImportCSVActivityTest {

    private val injector = KodeinInjector()
    private val reactiveTableRepository: ReactiveTableRepository by injector.instance()
    private val tableRepository: TableRepository by lazy { reactiveTableRepository.store }

    @Before
    fun setup() {
        injector.inject((RuntimeEnvironment.application as TaflanApplication).kodein)
    }

    @Test
    fun clickingAdd_createsTableFromFile() {
        val uri = createCSVFile("field1,field2\nhello,hi\ngoodbye,see ya")
        val intent = Intent(RuntimeEnvironment.application, ImportCSVActivity::class.java).apply {
            data = uri
        }

        val activity = buildActivity(ImportCSVActivity::class.java, intent).setup().get()

        (activity.form.fields.getChildAt(0) as EditText).setText("New Table")
        activity.form.submit.performClick()

        val tables = tableRepository.fetchAll()
        assertThat(tables.size).isEqualTo(1)

        val table = tables.first()
        assertThat(table.name).isEqualTo("New Table")
        assertThat(table.fields).isEqualTo(listOf("field1", "field2"))
        assertThat(table.items[0].values).isEqualTo(listOf("hello", "hi"))
        assertThat(table.items[1].values).isEqualTo(listOf("goodbye", "see ya"))
    }

    @Test
    fun clickingAdd_finishes() {
        val uri = createCSVFile("field1,field2\nhello,hi\ngoodbye,see ya")
        val intent = Intent(RuntimeEnvironment.application, ImportCSVActivity::class.java).apply {
            data = uri
        }

        val activity = buildActivity(ImportCSVActivity::class.java, intent).setup().get()

        (activity.form.fields.getChildAt(0) as EditText).setText("New Table")
        activity.form.submit.performClick()

        assertThat(activity.isFinishing).isTrue()
    }

    private fun createCSVFile(contents: String): Uri? {
        val context = RuntimeEnvironment.application
        val uri = Uri.parse("file://test")
        shadowOf(context.contentResolver).registerInputStream(uri, contents.byteInputStream())

        return uri
    }
}
