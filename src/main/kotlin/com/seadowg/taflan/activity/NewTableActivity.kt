package com.seadowg.taflan.activity

import android.app.Activity
import android.os.Bundle
import android.widget.EditText
import com.github.salomonbrys.kodein.instance
import com.seadowg.taflan.R
import com.seadowg.taflan.repository.TableRepository

class NewTableActivity : KodeinActivity() {

    private val tableRepository: TableRepository by injector.instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_table)

        findViewById(R.id.add).setOnClickListener {
            val nameField = findViewById(R.id.name) as EditText
            tableRepository.create(nameField.text.toString())

            finish()
        }
    }
}
