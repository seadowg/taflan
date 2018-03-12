package com.seadowg.taflan.activity

import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.view.Form

class EditTableActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_table)
        setupToolbar("", backArrow = true)

        val table = intent.getSerializableExtra(EditItemActivity.EXTRA_TABLE) as Table.Existing

        val form = findViewById<Form>(R.id.form)
        form.setup(listOf(Form.Field("Name", table.name, multiline = false)), "Update") { values ->
            tableRepository.change { it.save(table.copy(name = values[0])) }
            finish()
        }
    }

    companion object {
        val EXTRA_TABLE = "extra_table"
    }
}