package com.seadowg.taflan

import android.app.Application
import android.content.Context
import android.preference.PreferenceManager
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.seadowg.taflan.repository.SharedPreferencesTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.util.AndroidContentReader
import com.seadowg.taflan.util.ContentReader

open class TaflanApplication : Application() {

    lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()
        setupKodein()
    }

    fun setupKodein() {
        kodein = dependencies(this)
    }

    protected open fun dependencies(context: Context): Kodein {
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val tableRepository = SharedPreferencesTableRepository(preferences)

        return Kodein {
            bind<ContentReader>().with(singleton { AndroidContentReader(applicationContext) })
            bind<TableRepository>().with(singleton { tableRepository })
        }
    }
}
