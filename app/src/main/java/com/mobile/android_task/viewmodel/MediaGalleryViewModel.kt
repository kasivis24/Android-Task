package com.mobile.android_task.viewmodel

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.entities.PieChartData
import com.mobile.android_task.data.repository.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class MediaGalleryViewModel : ViewModel() {


    val database = MainApplication.database.AppDao()

    val fireStoreDB = FirebaseFirestore.getInstance()

    val repository = FolderRepository()

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

    private val _selectedItem = MutableStateFlow(-1)
    val selectedItem: StateFlow<Int> = _selectedItem

    private val _selectedFileItem = MutableStateFlow(FileData(0,"","","","","","","",""))
    val selectedFileItem: StateFlow<FileData> = _selectedFileItem

    private val _isChangeView = MutableStateFlow(false)
    val isChangeView: StateFlow<Boolean> = _isChangeView


    init {
        loadPieChartData()
    }

    fun setItemId(id : Int){
        _selectedItem.value = id
    }

    fun changeView(view : Boolean){
        _isChangeView.value = view
    }

    fun setFileData(fileData: FileData){
        _selectedFileItem.value = fileData
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





     fun uploadSelectedFolder(
         authToken: String,
         list: List<String>,
         fileData: MutableState<FileData>,
         isSuccess: () -> Unit,
         isFailure: () -> Unit,
     ){

        try {
            Log.d("Log", "mydata $fileData")
            list.forEachIndexed { index, s ->

                val docRef = fireStoreDB.collection("file").document()
                val docId = docRef.id
                val myData = FileData(fileData.value.id,s,fileData.value.fileName,fileData.value.createdDate,fileData.value.fileSize,fileData.value.fileUrl,fileData.value.fileType,authToken,fileData.value.fileId)
                    docRef.set(myData)
                    .addOnSuccessListener {
                        Log.d("Log", "File added with ID: $docId")
                        viewModelScope.launch(Dispatchers.IO) {
                            database.addFile(myData)
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.e("Log", "Failed to add: $exception")
                        isFailure()
                    }
            }
            isSuccess()
        } catch (e: Exception) {
            Log.e("Error", "Exception in Firestore: ${e.message}")
            error(e.message ?: "Unknown error")
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
         else {
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