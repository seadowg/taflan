package com.seadowg.taflan

import android.app.Application
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import com.seadowg.taflan.repository.InMemoryTableRepository
import com.seadowg.taflan.repository.TableRepository

class TaflanApplication : Application() {

    lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()

        kodein = Kodein {
            bind<TableRepository>().with(singleton { InMemoryTableRepository() })
        }
    }
}
