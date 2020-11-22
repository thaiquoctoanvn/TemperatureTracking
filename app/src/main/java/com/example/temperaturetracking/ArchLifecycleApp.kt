package com.example.temperaturetracking

import android.app.Application
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.temperaturetracking.di.apiModule
import com.example.temperaturetracking.di.temperatureRepository
import com.example.temperaturetracking.di.temperatureViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ArchLifecycleApp : Application(), LifecycleObserver {
    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        startKoin {
            androidContext(this@ArchLifecycleApp)
            modules(listOf(apiModule, temperatureRepository, temperatureViewModel))
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun moveToAppOnForeground() {
        Log.d("###", "app is on foreground")
        isAppInForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun moveToAppOnBackground() {
        Log.d("###", "app is on background")
        isAppInForeground = false
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun moveToAppDestroyed() = Lifecycle.Event.ON_DESTROY

    companion object {
        var isAppInForeground = false
    }
}