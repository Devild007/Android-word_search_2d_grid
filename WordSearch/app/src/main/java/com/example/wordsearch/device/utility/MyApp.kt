package com.example.wordsearch.device.utility

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import timber.log.Timber

class MyApp : MultiDexApplication() {

    companion object {
        var instance: MyApp? = null
        lateinit var appContext: Context

        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        appContext = applicationContext
        MultiDex.install(this)
        Timber.plant(Timber.DebugTree())
    }

}
