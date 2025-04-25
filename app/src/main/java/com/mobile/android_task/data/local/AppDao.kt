package com.mobile.android_task.data.local
import androidx.lifecycle.LiveData
import androidx.room.*
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FileSizeData
import com.mobile.android_task.data.entities.PieChartData
import com.mobile.android_task.data.entities.FolderData

@Dao
interface AppDao{

    // create folder
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createFolder(folderData: FolderData)

    // add files
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFile(fileData: FileData)

    // all folders
    @Query("SELECT * FROM folder")
    fun getFolder() : LiveData<List<FolderData>>

    // delete all data from folder
    @Query("DELETE FROM folder")
    suspend fun deleteAllFolders()

    // delete all data from files
    @Query("DELETE FROM file")
    suspend fun deleteAllFiles()

    // getFileData
    @Query("SELECT * FROM file where folderId = :folderId")
    suspend fun getFileData(folderId : String): List<FileData>

    // remove File folder
    @Query("DELETE FROM file where folderId = :folderId")
    suspend fun removeFilesById(folderId : String)

    // remove folder by Id
    @Query("DELETE FROM folder where folderId = :folderId")
    suspend fun removeFolderById(folderId : String)

    // remove SingleFile
    @Query("DELETE FROM file where folderId = :folderId AND fileId = :fileId")
    suspend fun removeSingleFilesById(folderId : String,fileId : String)

    // total Size Files
    @Query("SELECT fileType, SUM(fileSize) as totalSize FROM file GROUP BY fileType")
    suspend fun getFileSizesByType(): List<FileSizeData>


}
