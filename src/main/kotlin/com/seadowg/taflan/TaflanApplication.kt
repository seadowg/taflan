package com.seadowg.taflan

import android.app.Application
import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.seadowg.taflan.activity.TaflanActivity
import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.repository.SharedPreferencesTableRepository
import com.seadowg.taflan.repository.TableRepository

class TaflanApplication : Application() {

    lateinit var kodein: Kodein

    private lateinit var tableRepository: TableRepository

    override fun onCreate() {
        super.onCreate()

        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        tableRepository = SharedPreferencesTableRepository(preferences)

        kodein = Kodein {
            bind<TableRepository>().with(singleton { tableRepository })
        }
    }

    fun setupTest() {
        tableRepository.clear()
        TaflanActivity.TEST_MODE = true
    }
}
