package com.example.tasaagaovcps

import android.app.Application
import com.example.tasaagaovcps.data.AppContainer
import com.example.tasaagaovcps.data.AppContainerImpl

class TasaagaApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppContainerImpl()
    }
}
