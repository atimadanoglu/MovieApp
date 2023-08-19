package com.atakanmadanoglu.movieapp

import android.app.Application
import com.atakanmadanoglu.movieapp.di.dataModule
import com.atakanmadanoglu.movieapp.di.databaseModule
import com.atakanmadanoglu.movieapp.di.mapperModule
import com.atakanmadanoglu.movieapp.di.serviceModule
import com.atakanmadanoglu.movieapp.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MovieApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MovieApplication)
            modules(
                serviceModule,
                databaseModule,
                viewModelModule,
                dataModule,
                mapperModule
            )
        }
    }
}