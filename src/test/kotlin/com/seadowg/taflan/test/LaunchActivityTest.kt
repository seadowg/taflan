package com.seadowg.taflan.test

import com.seadowg.taflan.activity.LaunchActivity

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

import com.seadowg.taflan.BuildConfig

import org.junit.Assert.assertTrue
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(23))
class LaunchActivityTest {

    @Test
    fun testSomething() {
        assertTrue(Robolectric.setupActivity(LaunchActivity::class.java) != null)
    }
}
