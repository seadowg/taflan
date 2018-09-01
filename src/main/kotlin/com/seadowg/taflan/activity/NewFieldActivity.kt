package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.view.Form
import com.seadowg.taflan.view.colorDrawable

class NewFieldActivity : TaflanActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_field)

        val table = intent.extras.getSerializable(EXTRA_TABLE) as Table.Existing
        setupToolbar("Add Field", table.colorDrawable(this), backArrow = true)

        val form = findViewById<Form>(R.id.form)
        form.setup(listOf(Form.Field("Name", "", true)), "Add") { values ->
            tableRepository.addField(table.id, values.first())
            finish()
        }
    }

    companion object {
        private val EXTRA_TABLE = "extra_table"

        fun intent(context: Context, table: Table.Existing): Intent {
            val intent = Intent(context, NewFieldActivity::class.java)
            intent.putExtra(NewFieldActivity.EXTRA_TABLE, table)

            return intent
        }
    }
}