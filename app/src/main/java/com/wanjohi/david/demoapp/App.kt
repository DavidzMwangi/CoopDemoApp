package com.wanjohi.david.demoapp

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.wanjohi.david.demoapp.di.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        startKoin {
            androidContext(this@App)
            modules(
                listOf(
                     networkModule, apiModule, repoModule, viewModelModule,
                    databaseModule
                )
            )
        }


    }
}