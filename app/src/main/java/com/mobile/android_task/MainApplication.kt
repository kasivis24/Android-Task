package com.mobile.android_task

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.mobile.android_task.data.local.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application(){
    companion object{
        lateinit var database: AppDatabase
    }
    override fun onCreate() {
        super.onCreate()
        Log.d("Screen","initapp")
        database = Room.databaseBuilder(applicationContext,AppDatabase::class.java,AppDatabase.DATABASE_NAME).build()
    }
}