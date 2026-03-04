package com.robertcoding.paginationnews

import android.app.Application
import com.robertcoding.data.network.networkModule
import com.robertcoding.data.repository.repositoryModule
import com.robertcoding.paginationnews.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class PaginationApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PaginationApplication)
            androidLogger(Level.ERROR)
            modules(appModule, networkModule, repositoryModule)
        }
    }
}