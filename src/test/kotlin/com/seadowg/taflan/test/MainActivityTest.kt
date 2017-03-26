package com.seadowg.taflan.test

import com.seadowg.taflan.activity.MainActivity

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

import com.seadowg.taflan.BuildConfig

import org.junit.Assert.assertTrue
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(23))
class MainActivityTest {

    @Test
    fun testSomething() {
        assertTrue(Robolectric.setupActivity(MainActivity::class.java) != null)
    }
}
