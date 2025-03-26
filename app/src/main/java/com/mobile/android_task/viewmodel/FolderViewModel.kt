package com.mobile.android_task.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.repository.FolderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class FolderViewModel : ViewModel(){



    private val repository = FolderRepository()

    val folderLiveData: LiveData<List<FolderData>> = repository.getUsers()

    val database = MainApplication.database.AppDao()

    val fireStoreDB = FirebaseFirestore.getInstance()

    val todayDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())


    init {
        Log.d("Test-Android","Init Folder MVVM")
    }


    fun createFolder(folderName : String,onSuccess : ()-> Unit,onFailure : () -> Unit) {
        viewModelScope.launch(Dispatchers.IO){

            val docRef = fireStoreDB.collection("folder").document()  // generates new doc ref with ID
            val docId = docRef.id

            docRef.set(FolderData(Random.nextLong(),docId,folderName,0,todayDate,0))
                .addOnSuccessListener {
                    viewModelScope.launch(Dispatchers.IO){
                        Log.d("Log", "Document added with ID: $docId")
                        database.createFolder(FolderData(Random.nextLong(),docId,folderName,0,todayDate,0))
                        onSuccess()
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("Log", "Failed to add: $exception")
                    onFailure()
                }

        }
    }












}