package com.mobile.android_task.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.PieChartData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MediaGalleryViewModel : ViewModel() {


    val database = MainApplication.database.AppDao()

    val fireStoreDB = FirebaseFirestore.getInstance()


    private val _text = MutableLiveData("")
    val text: LiveData<String> = _text

    private val _fileId = MutableLiveData("")
    val fileId: LiveData<String> = _fileId

    private val _isSingleFile = MutableLiveData(false)
    val isSingleFile: LiveData<Boolean> = _isSingleFile

    private val _infoFileData = MutableLiveData(FileData())
    val infoFileData: LiveData<FileData> = _infoFileData

    private val _pieChartData = MutableLiveData<List<PieChartData>>()
    val pieChartData: LiveData<List<PieChartData>> = _pieChartData

    init {
        loadPieChartData()
    }

    fun loadPieChartData() {
        viewModelScope.launch {
            val fileSizes = database.getFileSizesByType()
            val pieData = fileSizes.map { data ->
                PieChartData(
                    name = when {
                        data.fileType.startsWith("image/") -> "Image"
                        data.fileType.startsWith("audio/") -> "Audio"
                        data.fileType.startsWith("video/") -> "Video"
                        else -> "Other"
                    },
                    value = data.totalSize
                )
            }
            _pieChartData.postValue(pieData)
        }
    }

    fun infoFileData(fileData: FileData) {
        viewModelScope.launch {
            _infoFileData.postValue(fileData)
        }
    }


    fun copyFolder(folderId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _text.postValue(folderId)
            _isSingleFile.postValue(false)
            onSuccess()
        }
    }

    fun copySingleFile(folderId: String, fileId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _text.postValue(folderId)
            _fileId.postValue(fileId)
            _isSingleFile.postValue(true)
            onSuccess()
        }
    }


    fun deleteSingleFile(
        folderId: String,
        fileId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        viewModelScope.launch {
            viewModelScope.launch(Dispatchers.IO) {

                fireStoreDB.collection("file")
                    .whereEqualTo("folderId", folderId)
                    .whereEqualTo("fileId", fileId)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            fireStoreDB.collection("file").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    viewModelScope.launch(Dispatchers.IO) {
                                        database.removeSingleFilesById(folderId, fileId)
                                    }
                                    Log.d("Log", "Document ${document.id} successfully deleted!")
                                }
                                .addOnFailureListener { e ->
                                    Log.w("Log", "Error deleting document", e)
                                }
                        }
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        Log.w("Log", "Error finding documents", e)
                        onFailure()
                    }
            }
        }
    }

    suspend fun pasteFolder(
        authToken: String,
        folderId: String,
        isSuccess: () -> Unit,
        isFailure: () -> Unit,
        error: (String) -> Unit,
    ) {


        Log.e("Log", "FileId ${fileId.value} siSingleFile ${isSingleFile.value}")

        Log.d("Log","paste")

        if (!folderId.isEmpty() && text.value?.isNotEmpty() == true && !isSingleFile.value!!) {
            Log.d("Log", "Paste Scope folderId $folderId")

            try {
                val querySnapshot = fireStoreDB.collection("file")
                    .whereEqualTo("authToken", authToken)
                    .whereEqualTo("folderId", text.value)
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    val docRef = fireStoreDB.collection("file").document()
                    val docId = docRef.id
                    val myData = FileData(
                        document.get("id")?.toString()?.toIntOrNull() ?: 0,
                        folderId,
                        document.getString("fileName").orEmpty(),
                        document.getString("createdDate").orEmpty(),
                        document.getString("fileSize").orEmpty(),
                        document.getString("fileUrl").orEmpty(),
                        document.getString("fileType").orEmpty(),
                        document.getString("authToken").orEmpty(),
                        document.getString("fileId").orEmpty()
                    )
                    Log.d("Log", "mydata $myData")

                    docRef.set(myData)
                        .addOnSuccessListener {
                            Log.d("Log", "File added with ID: $docId")
                            viewModelScope.launch(Dispatchers.IO) {
                                database.addFile(myData)
                                withContext(Dispatchers.Main) {
                                    isSuccess()
                                }
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.e("Log", "Failed to add: $exception")
                            isFailure()
                        }
                }
            } catch (e: Exception) {
                Log.e("Error", "Exception in Firestore: ${e.message}")
                error(e.message ?: "Unknown error")
            }
        }
        else if (!folderId.isEmpty() && text.value?.isNotEmpty() == true && isSingleFile.value!! && fileId.value?.isNotEmpty() == true) {
            Log.d("Log", "Paste Scope Single fileId ${fileId.value} authToken ${authToken}")

            try {

                fireStoreDB.collection("file")
                    .whereEqualTo("authToken", authToken)
                    .whereEqualTo("folderId", text.value)
                    .whereEqualTo("fileId", fileId.value)
                    .get()
                    .addOnSuccessListener {
                        Log.d("Log"," documnet $authToken")

                        for (document in it.documents) {

                            Log.d("Log"," documnet $document")

                            val docRef = fireStoreDB.collection("file").document()
                            val docId = docRef.id
                            val myData = FileData(
                                document.get("id")?.toString()?.toIntOrNull() ?: 0,
                                folderId,
                                document.getString("fileName").orEmpty(),
                                document.getString("createdDate").orEmpty(),
                                document.getString("fileSize").orEmpty(),
                                document.getString("fileUrl").orEmpty(),
                                document.getString("fileType").orEmpty(),
                                document.getString("authToken").orEmpty(),
                                document.getString("fileId").orEmpty()
                            )
                            Log.d("Log", "mydata $myData")

                            docRef.set(myData)
                                .addOnSuccessListener {
                                    Log.d("Log", "File added with ID: $docId")
                                    viewModelScope.launch(Dispatchers.IO) {
                                        database.addFile(myData)
                                        withContext(Dispatchers.Main) {
                                            isSuccess()
                                        }
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Log.e("Log", "Failed to add: $exception")
                                    isFailure()
                                }
                        }
                    }

//                withContext(Dispatchers.Main) {
//                    error("error found after try auth token ${querySnapshot.documents}")
//                }
//                withContext(Dispatchers.Main) {
//                    error("error found after try text folder ${text.value}")
//                }
//                withContext(Dispatchers.Main) {
//                    error("error found after try file id ${fileId}")
//                }
//                withContext(Dispatchers.Main) {
//                    error("error found after try folder d ${folderId}")
//                }
//
//                withContext(Dispatchers.Main) {
//                    error("error found after await")
//                }
//
//                withContext(Dispatchers.Main) {
//                    error("error found after await ${querySnapshot.documents}")
//                }


            } catch (e: Exception) {
                Log.e("Error", "Exception in Firestore: ${e.message}")
                error(e.message ?: "Unknown error")
            }
        } else {
            error("Invalid data or empty fields")
        }
    }




    fun deleteAllFilesFromFolder(folderId: String,onSuccess: () -> Unit,onFailure: ()-> Unit){
        viewModelScope.launch(Dispatchers.IO){

            fireStoreDB.collection("file")
                .whereEqualTo("folderId", folderId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        fireStoreDB.collection("file").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                viewModelScope.launch(Dispatchers.IO){
                                    database.removeFilesById(folderId)
                                }
                                Log.d("Log", "Document ${document.id} successfully deleted!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Log", "Error deleting document", e)
                            }
                    }
                    onSuccess()
                }
                .addOnFailureListener { e ->
                    Log.w("Log", "Error finding documents", e)
                    onFailure()
                }
        }
    }






}