package com.example.myapplication.application

import android.app.Application
import com.example.myapplication.ApplicationComponent
import com.example.myapplication.DaggerApplicationComponent

class MainApp : Application() {

    lateinit var appComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerApplicationComponent.create()
    }

}