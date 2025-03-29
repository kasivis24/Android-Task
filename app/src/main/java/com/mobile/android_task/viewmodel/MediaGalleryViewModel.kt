package com.mobile.android_task.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MediaGalleryViewModel : ViewModel() {


    val database = MainApplication.database.AppDao()

    val fireStoreDB = FirebaseFirestore.getInstance()

    val auth = FirebaseAuth.getInstance()

    val authToken = auth.currentUser?.uid

    private val _text = MutableLiveData("")
    val text: LiveData<String> = _text

    private val _fileId = MutableLiveData("")
    val fileId: LiveData<String> = _fileId

    private val _isSingleFile = MutableLiveData(false)
    val isSingleFile: LiveData<Boolean> = _isSingleFile





    fun copyFolder(folderId : String,onSuccess: () -> Unit){
        viewModelScope.launch {
            _text.value = folderId
            _isSingleFile.value = false
            onSuccess()
        }
    }

    fun copySingleFile(folderId: String,fileId : String,onSuccess: () -> Unit){
        viewModelScope.launch {
            _text.value = folderId
            _fileId.value = fileId
            _isSingleFile.value = true
            onSuccess()
        }
    }


    fun deleteSingleFile(folderId: String,fileId: String,onSuccess: () -> Unit,onFailure: () -> Unit){
        viewModelScope.launch {
            viewModelScope.launch {

                fireStoreDB.collection("file")
                    .whereEqualTo("folderId", folderId)
                    .whereEqualTo("fileId",fileId)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            fireStoreDB.collection("file").document(document.id)
                                .delete()
                                .addOnSuccessListener {
                                    viewModelScope.launch(Dispatchers.IO){
                                        database.removeSingleFilesById(folderId,fileId)
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

    fun pasteFolder(folderId: String,isSuccess: ()-> Unit,isFailure: ()-> Unit){

        if (!folderId.isEmpty() && text.value?.isNotEmpty() == true && isSingleFile.value == false){
            Log.d("Log","Paste Scope folderId ${folderId}")

            viewModelScope.launch {

                try {
                    Log.d("Log","uuid ${authToken}  && ${text.value}")
                    val querySnapshot = fireStoreDB.collection("file").whereEqualTo("authToken",authToken).whereEqualTo("folderId",text.value).get().await() // Use await()

                    for (document in querySnapshot.documents) {
                        val docRef = fireStoreDB.collection("file").document()  // generates new doc ref with ID
                        val docId = docRef.id
                        val myData = FileData(document.get("id").toString().toInt(),folderId,document.getString("fileName").toString(),document.getString("createdDate").toString(),document.getString("fileSize").toString(), document.getString("fileUrl").toString(),document.getString("fileType").toString(),document.getString("authToken").toString(),document.getString("fileId").toString())
                        Log.d("Log","mydata ${myData}")
                        docRef.set(myData)
                            .addOnSuccessListener {
                                viewModelScope.launch(Dispatchers.IO){
                                    Log.d("Log", "File added with ID: $docId")
                                    database.addFile(myData)
                                    isSuccess()
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("Log", "Failed to add: $exception")
                                isFailure()
                            }
                    }



                } catch (e: Exception) {
                    // Handle errors appropriately (e.g., log, show error message)
                    println("Error fetching data: ${e.message}")
                }


            }
        }

        if (!folderId.isEmpty() && text.value?.isNotEmpty() == true && isSingleFile.value == true && fileId.value?.isNotEmpty() == true){
            Log.d("Log","Paste Scope fileId ${fileId}")

            viewModelScope.launch {

                try {
                    Log.d("Log","uuid ${authToken}  && ${text.value}")
                    val querySnapshot = fireStoreDB.collection("file").whereEqualTo("authToken",authToken).whereEqualTo("folderId",text.value).whereEqualTo("fileId",fileId.value).get().await() // Use await()

                    for (document in querySnapshot.documents) {
                        val docRef = fireStoreDB.collection("file").document()  // generates new doc ref with ID
                        val docId = docRef.id
                        val myData = FileData(document.get("id").toString().toInt(),folderId,document.getString("fileName").toString(),document.getString("createdDate").toString(),document.getString("fileSize").toString(), document.getString("fileUrl").toString(),document.getString("fileType").toString(),document.getString("authToken").toString(),document.getString("fileId").toString())
                        Log.d("Log","mydata ${myData}")
                        docRef.set(myData)
                            .addOnSuccessListener {
                                viewModelScope.launch(Dispatchers.IO){
                                    Log.d("Log", "File added with ID: $docId")
                                    database.addFile(myData)
                                    isSuccess()
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("Log", "Failed to add: $exception")
                                isFailure()
                            }
                    }



                } catch (e: Exception) {
                    // Handle errors appropriately (e.g., log, show error message)
                    println("Error fetching data: ${e.message}")
                }


            }
        }
    }


    fun deleteAllFilesFromFolder(folderId: String,onSuccess: () -> Unit,onFailure: ()-> Unit){
        viewModelScope.launch {

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