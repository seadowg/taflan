package com.seadowg.taflan.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.domain.Item
import com.seadowg.taflan.domain.Table
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.view.Form
import com.seadowg.taflan.view.colorDrawable

class NewItemActivity : TaflanActivity() {

    private val tableRepository: ReactiveTableRepository by injector.instance()

    private val tableID: String by lazy { intent.extras.getString(NewItemActivity.EXTRA_TABLE) }
    private val table by lazy { tableRepository.store.fetch(tableID)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_item)

        setupToolbar("Add Item", color = table.colorDrawable(this), backArrow = true)

        val form = findViewById<Form>(R.id.form)
        form.setup(table.fields.map { Form.Field(it, "", true) }, "Add") { values ->
            tableRepository.change { it.addItem(table.id, Item.New(values)) }
            finish()
        }
    }

    companion object {
        private val EXTRA_TABLE = "extra_table"

        fun intent(context: Context, table: Table.Existing): Intent {
            val intent = Intent(context, NewItemActivity::class.java)
            intent.putExtra(NewItemActivity.EXTRA_TABLE, table.id)

            return intent
        }
    }
}