package com.seadowg.taflan.test

import android.preference.PreferenceManager
import com.seadowg.taflan.BuildConfig
import com.seadowg.taflan.repository.SharedPreferencesTableRepository
import com.seadowg.taflan.repository.TableRepository
import com.seadowg.taflan.test.contract.TableRepositoryTest
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
class SharedPreferencesTableRepositoryTest : TableRepositoryTest() {

    override val tableRepository: TableRepository
        get() {
            val context = RuntimeEnvironment.application
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return SharedPreferencesTableRepository(preferences)
        }

}