package com.seadowg.taflan.test.activity

import android.graphics.drawable.ColorDrawable
import android.widget.Toolbar
import com.seadowg.taflan.R
import com.seadowg.taflan.activity.EditTableActivity
import com.seadowg.taflan.domain.Color
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.test.support.backgroundColor
import com.seadowg.taflan.view.colorDrawable
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class EditTableActivityTest {

    @Test
    fun stylesToolbarForTable() {
        val table = Table.Existing(
                id = "1", name = "Table", color = Color.Blue, fields = listOf("Name"), items = emptyList()
        )

        val intent = EditTableActivity.intent(RuntimeEnvironment.application, table)

        val activity = Robolectric.buildActivity(EditTableActivity::class.java, intent).setup().get()
        val toolbar = activity.findViewById<Toolbar>(R.id.toolbar)

        assertThat(toolbar.backgroundColor()).isEqualTo(R.color.card_blue)
    }
}
