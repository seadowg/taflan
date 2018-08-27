package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.bind
import com.seadowg.taflan.util.reactive
import com.seadowg.taflan.view.Form
import com.seadowg.taflan.view.colorDrawable
import kotlinx.android.synthetic.main.edit_table.*

class EditTableActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    private val table by lazy { intent.getSerializableExtra(EditTableActivity.EXTRA_TABLE) as Table.Existing }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_table)

        setupToolbar("Edit Table", color = table.colorDrawable(this), backArrow = true)

        form.setup(listOf(Form.Field("Name", table.name, multiline = false)), "Update") { values ->
            tableRepository.save(table.copy(name = values[0]))
            finish()
        }

        delete.reactive().clicks.bind(this) {
            tableRepository.delete(table)
            navigator.returnToTables()
        }
    }

    companion object {
        private val EXTRA_TABLE = "extra_table"

        fun intent(context: Context, table: Table.Existing): Intent {
            return Intent(context, EditTableActivity::class.java)
                    .putExtra(EditTableActivity.EXTRA_TABLE, table)
        }
    }
}
