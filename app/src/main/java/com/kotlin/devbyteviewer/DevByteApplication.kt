package com.kotlin.devbyteviewer

import android.app.Application
import timber.log.Timber

// for some reason it can't see that this is used
@Suppress("unused")
class DevByteApplication : Application() {

    // This is called before anything is shown to the user. Use it to setup any background tasks,
    // running expensive setup operations in a background thread to avoid delaying app start.
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}
