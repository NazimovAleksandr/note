package com.notes

import android.app.Application
import com.notes.di.AppComponent
import com.notes.di.DaggerAppComponent

class App : Application() {
    val appComponent: AppComponent = DaggerAppComponent
        .factory()
        .create(this)
}