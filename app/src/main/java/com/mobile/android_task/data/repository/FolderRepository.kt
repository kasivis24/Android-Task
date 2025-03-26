package com.mobile.android_task.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.mobile.android_task.data.entities.FolderData

class FolderRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getUsers(): LiveData<List<FolderData>> {
        val liveData = MutableLiveData<List<FolderData>>()

        db.collection("folder")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    liveData.value = emptyList()
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    val userList = snapshot.documents.map { document ->
                        document.toObject(FolderData::class.java)!!
                    }
                    liveData.value = userList
                }
            }

        return liveData
    }
}
