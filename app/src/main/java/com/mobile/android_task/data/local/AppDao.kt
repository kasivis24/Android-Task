package com.mobile.android_task.data.local
import androidx.room.*
import com.mobile.android_task.data.entities.FolderData

@Dao
interface AppDao{

    // create folder
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createFolder(folderData: FolderData)


}
