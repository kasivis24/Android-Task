package com.mobile.android_task.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.mobile.android_task.MainApplication
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.repository.FolderRepository

class SearchViewModel : ViewModel() {

    var searchQuery = mutableStateOf("")

    private val database = MainApplication.database.AppDao()

    val folderList: LiveData<List<FolderData>> = database.getFolder()

    val filteredFolderList = MediatorLiveData<List<FolderData>>().apply {
        addSource(folderList) { updateFilteredFolder() }
    }



    fun updateFilteredFolder() {
        Log.d("Log","folderdata ${folderList.value}")
        val query = searchQuery.value.lowercase()
        filteredFolderList.value = folderList.value?.filter {
            it.folderName.lowercase().contains(query)
        } ?: emptyList()
    }



}