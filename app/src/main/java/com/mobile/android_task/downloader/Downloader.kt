package com.mobile.android_task

interface Downloader {
    fun downloadFile(url: String,folderName : String): Long
}