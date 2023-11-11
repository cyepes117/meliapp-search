package com.meliapp.search

import android.app.Application
import com.meliapp.search.data.di.getDataModule
import com.meliapp.search.di.getPresentationModule
import com.meliapp.search.domain.di.getDomainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AppApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppApplication)
            modules(
                getDataModule(),
                getDomainModule(),
                getPresentationModule(),
            )
        }
    }
}