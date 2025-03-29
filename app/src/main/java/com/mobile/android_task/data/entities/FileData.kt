package com.mobile.android_task.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mobile.android_task.data.local.FileType


@Entity(tableName = "file")
data class FileData(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val folderId : String = "",
    val fileName : String = "",
    val createdDate : String = "",
    val fileSize: String = "",
    val fileUrl : String = "",
    val fileType : String = "",
    val authToken : String = "",
    val fileId : String = "",
){
    fun determineFileType(): FileType {
        return when {
            fileType.startsWith("image/") -> FileType.IMAGE
            fileType.startsWith("audio/") -> FileType.AUDIO
            fileType.startsWith("video/") -> FileType.VIDEO
            else -> FileType.DOCUMENT
        }
    }
}
