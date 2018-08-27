package com.seadowg.taflan

import android.content.Context
import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.*
import com.nhaarman.mockito_kotlin.mock
import com.seadowg.taflan.repository.ReactiveTableRepository
import com.seadowg.taflan.repository.SharedPreferencesTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.tracking.Tracker
import com.seadowg.taflan.util.AndroidContentReader
import com.seadowg.taflan.util.ContentReader

class TestTaflanApplication : TaflanApplication() {

    override fun dependencies(context: Context): Kodein {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val tableRepository = SharedPreferencesTableRepository(preferences)

        return Kodein {
            bind<ContentReader>().with(singleton { AndroidContentReader(applicationContext) })
            bind<TableRepository>().with(singleton { tableRepository })
            bind<ReactiveTableRepository>().with(singleton { ReactiveTableRepository(tableRepository) })
            bind<Tracker>().with(singleton { mock<Tracker>() })
        }
    }
}
