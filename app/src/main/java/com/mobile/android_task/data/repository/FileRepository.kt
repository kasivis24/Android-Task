package com.mobile.android_task.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData


class FileRepository {

    private val db = FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    private val userId = auth.currentUser?.uid

    fun getData(folderid : String): LiveData<List<FileData>> {
        val liveData = MutableLiveData<List<FileData>>()

        db.collection("file")
            .whereEqualTo("authToken",userId)
            .whereEqualTo("folderId",folderid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val userList = snapshot.documents.map { document ->
                        Log.d("Log","Snap ${document.data?.get("authToken").toString()}")
                        document.toObject(FileData::class.java)!!
                    }
                    liveData.value = userList
                }
            }

        return liveData
    }
}
