package com.mobile.android_task.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.OpenableColumns
import android.text.format.Time
import android.util.Log
import android.util.TimeUtils
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.type.DateTime
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.data.repository.FileRepository
import com.mobile.android_task.data.repository.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import java.util.Timer
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


    fun resetDbFile(dataList : List<FileData>){
        dataList.forEach { fileData ->
            viewModelScope.launch(Dispatchers.IO){
                Log.d("Log","added ${fileData}")
                database.addFile(fileData)
            }
        }
    }


    /// for Reset the DB
    suspend fun fetchDataFromFireStoreFile(uId: String) : SnapshotStateList<FileData> {

        val firestore = FirebaseFirestore.getInstance()
        val dataList = mutableStateListOf<FileData>()

        try {
            Log.d("Log","uuid ${uId}")
            val querySnapshot = firestore.collection("file").whereEqualTo("authToken",uId).get().await() // Use await()

            for (document in querySnapshot.documents) {
                val myData = FileData(document.get("id").toString().toInt(),document.getString("folderId").toString(),document.getString("fileName").toString(),document.get("createdDate").toString(),document.getString("fileSize").toString(), document.getString("fileUrl").toString(),document.getString("fileType").toString(),document.getString("authToken").toString(),document.getString("fileId").toString())
                dataList.add(myData)
            }

            Log.d("Log","from ${dataList}")

        } catch (e: Exception) {
            // Handle errors appropriately (e.g., log, show error message)
            println("Error fetching data: ${e.message}")
        }

        return dataList
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


    @RequiresApi(Build.VERSION_CODES.O)
    fun uploadTheFiles(allSelectedList : List<FileData>, folderId: String, isSuccess : () -> Unit, isFailure : () -> Unit){
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("hh-mm-ss"))

        viewModelScope.launch (Dispatchers.IO){
            allSelectedList.forEachIndexed { index, fileData ->
                val fileRef = storageRef.reference.child("FilesStorage/${System.currentTimeMillis()}_${fileData.fileUrl}")
                fileRef.putFile(Uri.parse(fileData.fileUrl)).addOnSuccessListener {
                    fileRef.downloadUrl.addOnSuccessListener { fileUrl ->

                        val docRef = fireStoreDB.collection("file").document()  // generates new doc ref with ID
                        val docId = docRef.id

                        docRef.set(FileData(0,folderId,fileData.fileName,fileData.createdDate,fileData.fileSize,fileUrl.toString(),fileData.fileType,"$userId",docId,"${time}"))
                            .addOnSuccessListener {
                                viewModelScope.launch(Dispatchers.IO){
                                    Log.d("Log", "File added with ID: $docId")
                                    Log.d("Log","$time")
                                    database.addFile(FileData(0,folderId,fileData.fileName,fileData.createdDate,fileData.fileSize,fileUrl.toString(),fileData.fileType,"$userId",docId,"${time}"))
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

     @RequiresApi(Build.VERSION_CODES.O)
     fun getFileMetadata(context: Context, uri: Uri, folderId : String): FileData {

        val contentResolver = context.contentResolver
        var name = "Unknown"
        var size: Long = 0
        var mimeType = contentResolver.getType(uri) ?: "Unknown"
        val time = LocalTime.now()

        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)

            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex) ?: "Unknown"
                size = cursor.getLong(sizeIndex)
            }
        }

         Log.d("Time","${time}")

        return FileData(0,folderId,name,todayDate, formatFileSize(size), uri.toString(),mimeType,"$userId","${time}")
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