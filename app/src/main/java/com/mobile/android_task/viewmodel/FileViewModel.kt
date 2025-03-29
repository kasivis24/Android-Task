package com.mobile.android_task.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.data.repository.FileRepository
import com.mobile.android_task.data.repository.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FileViewModel : ViewModel() {

    private val repository = FileRepository()

    val storageRef = FirebaseStorage.getInstance()

    val database = MainApplication.database.AppDao()

    val fireStoreDB = FirebaseFirestore.getInstance()

    val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    val auth = FirebaseAuth.getInstance()

    val userId = auth.currentUser?.uid


    private val _fileDataLiveDataCloud = MutableLiveData<List<FileData>>()
    val fileDataLiveDataCloud: LiveData<List<FileData>> = _fileDataLiveDataCloud

    private val _fileDataListLocal = MutableLiveData<List<FileData>>()
    val fileDataListLocal: LiveData<List<FileData>> = _fileDataListLocal


    init {
        Log.d("Test-Android","Init File MVVM")
    }


    fun getFileDataCloud(folderId: String){
        repository.getData(folderId).observeForever { data ->
            _fileDataLiveDataCloud.value = data
        }
    }


    fun getFileDataLocal(folderId: String){
        viewModelScope.launch {
            val data = database.getFileData(folderId)
            _fileDataListLocal.postValue(data)
        }
    }


    fun uploadTheFiles(allSelectedList : List<FileData>,folderId: String,isSuccess : () -> Unit,isFailure : () -> Unit){

        viewModelScope.launch (Dispatchers.IO){
            allSelectedList.forEachIndexed { index, fileData ->
                val fileRef = storageRef.reference.child("FilesStorage/${System.currentTimeMillis()}_${fileData.fileUrl}")
                fileRef.putFile(Uri.parse(fileData.fileUrl)).addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { fileUrl ->

                        val docRef = fireStoreDB.collection("file").document()  // generates new doc ref with ID
                        val docId = docRef.id

                        docRef.set(FileData(0,folderId,fileData.fileName,fileData.createdDate,fileData.fileSize,fileUrl.toString(),fileData.fileType,"$userId",docId))
                            .addOnSuccessListener {
                                viewModelScope.launch(Dispatchers.IO){
                                    Log.d("Log", "File added with ID: $docId")
                                    database.addFile(FileData(0,folderId,fileData.fileName,fileData.createdDate,fileData.fileSize,fileUrl.toString(),fileData.fileType,"$userId",docId))
                                    isSuccess()
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("Log", "Failed to add: $exception")
                                isFailure()
                            }


                    }
                }
            }
        }

    }

     fun getFileMetadata(context: Context, uri: Uri, folderId : String): FileData {

        val contentResolver = context.contentResolver
        var name = "Unknown"
        var size: Long = 0
        var mimeType = contentResolver.getType(uri) ?: "Unknown"

        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex) ?: "Unknown"
                size = cursor.getLong(sizeIndex)
            }
        }


        return FileData(0,folderId,name,todayDate, formatFileSize(size), uri.toString(),mimeType,"$userId")
    }

     private fun formatFileSize(size: Long): String {
        if (size <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
        return String.format("%.2f %s", size / Math.pow(1024.0, digitGroups.toDouble()), units[digitGroups])
    }


    fun removeAllFiles(){
        viewModelScope.launch {
            database.deleteAllFiles()
        }
    }

}