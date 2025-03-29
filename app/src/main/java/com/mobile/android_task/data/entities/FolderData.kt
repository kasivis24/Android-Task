package com.mobile.android_task.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "folder")
data class FolderData(
    @PrimaryKey(autoGenerate = true) val id : Long = 0L,
    val folderId : String = "",
    val folderName : String = "",
    val itemCount : Int = 0,
    val createdDate : String = "",
    val folderSize : Long = 0L,
    val authToken : String = "",
)