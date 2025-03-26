package com.mobile.android_task.data.local
import androidx.room.Database
import androidx.room.RoomDatabase
import com.mobile.android_task.data.entities.FolderData


@Database(entities = [FolderData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun AppDao(): AppDao
    companion object {
        const val DATA_NAME = "cloud_database"
    }
}


