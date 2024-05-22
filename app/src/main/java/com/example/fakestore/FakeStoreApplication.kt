package com.example.fakestore

import android.app.Application
import com.example.fakestore.di.AppModule
import com.example.fakestore.di.AppModuleImpl

class FakeStoreApplication: Application() {
    companion object{
         lateinit var appModule: AppModule
    }

    override fun onCreate() {
        super.onCreate()
        appModule = AppModuleImpl()
    }
}