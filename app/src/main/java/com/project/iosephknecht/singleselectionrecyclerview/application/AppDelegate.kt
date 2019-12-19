package com.project.iosephknecht.singleselectionrecyclerview.application

import android.app.Application

internal class AppDelegate : Application(),
    SingletonComponentHolder {

    private lateinit var singletonComponent: SingletonComponent

    override fun onCreate() {
        super.onCreate()

        singletonComponent = DaggerSingletonComponent.builder()
            .appContext(this)
            .build()
    }

    override fun getSingletonComponentHolder() = singletonComponent
}