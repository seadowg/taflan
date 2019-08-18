package com.seadowg.taflan.activity

import android.os.Bundle
import com.seadowg.taflan.R

class MainActivity : TaflanActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        setupToolbar(getString(R.string.app_title))
    }
}