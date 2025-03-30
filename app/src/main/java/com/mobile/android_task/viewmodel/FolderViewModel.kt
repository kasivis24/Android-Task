package com.mobile.android_task.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.repository.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FolderViewModel : ViewModel(){


    val database = MainApplication.database.AppDao()

    private val repository = FolderRepository()

    val folderLiveData: LiveData<List<FolderData>> = repository.getData()

    val folderLocalLiveData : LiveData<List<FolderData>> = database.getFolder()

    val fireStoreDB = FirebaseFirestore.getInstance()

    val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())

    val auth = FirebaseAuth.getInstance()

    val userId = auth.currentUser?.uid




    init {
        Log.d("Test-Android","Init Folder MVVM")
    }

    fun resetDbFolder(dataList : List<FolderData>){
        dataList.forEach {folderData ->
            viewModelScope.launch(Dispatchers.IO){
                Log.d("Log","added ${folderData}")
                database.createFolder(folderData)
            }
        }
    }


    fun deleteFolderAndItsFile(folderId : String){
        viewModelScope.launch(Dispatchers.IO){
            database.removeFilesById(folderId)
            database.removeFolderById(folderId)
            deleteFolderAndItsFileCloud(folderId)
        }
    }

    fun deleteFolderAndItsFileCloud(folderId: String){
        viewModelScope.launch {

            fireStoreDB.collection("file")
                .whereEqualTo("folderId", folderId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        fireStoreDB.collection("file").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("Log", "Document ${document.id} successfully deleted!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Log", "Error deleting document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Log", "Error finding documents", e)
                }
        }

        viewModelScope.launch {

            fireStoreDB.collection("folder")
                .whereEqualTo("folderId", folderId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        fireStoreDB.collection("folder").document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("Log", "Document ${document.id} successfully deleted!")
                            }
                            .addOnFailureListener { e ->
                                Log.w("Log", "Error deleting document", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Log", "Error finding documents", e)
                }
        }
    }






    suspend fun fetchDataFromFirestore(uId : String): SnapshotStateList<FolderData> {
        val firestore = FirebaseFirestore.getInstance()
        val dataList = mutableStateListOf<FolderData>()

        try {
            Log.d("Log","uuid ${uId}")
            val querySnapshot = firestore.collection("folder").whereEqualTo("authToken",uId).get().await() // Use await()

            for (document in querySnapshot.documents) {
                val myData = FolderData(document.getLong("id")?.toLong() ?: 0L,document.getString("folderId").toString(),document.getString("folderName").toString(),document.get("itemCount").toString().toInt(),document.getString("createdDate").toString(), document.getLong("folderSize")?.toLong() ?: 0L,document.getString("authToken").toString())
                dataList.add(myData)
            }

            Log.d("Log","from ${dataList}")

        } catch (e: Exception) {
            // Handle errors appropriately (e.g., log, show error message)
            println("Error fetching data: ${e.message}")
        }

        return dataList
    }



    fun createFolder(folderName : String,onSuccess : ()-> Unit,onFailure : () -> Unit) {
        Log.d("Log","uuid ${userId}")
        viewModelScope.launch(Dispatchers.IO){

            val docRef = fireStoreDB.collection("folder").document()  // generates new doc ref with ID
            val docId = docRef.id
            docRef.set(FolderData(Random.nextLong(),docId,folderName,0,todayDate,0,"${userId}"))
                .addOnSuccessListener {
                    viewModelScope.launch(Dispatchers.IO){
                        Log.d("Log", "Document added with ID: $docId")
                        database.createFolder(FolderData(Random.nextLong(),docId,folderName,0,todayDate,0,"${userId}"))
                        onSuccess()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Log", "Failed to add: $exception")
                    onFailure()
                }

        }
    }


    fun removeAllFolder(){
        viewModelScope.launch {
            database.deleteAllFolders()
        }
    }











}