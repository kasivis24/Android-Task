package com.mobile.android_task.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.android_task.AndroidDownloader
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.ui.theme.screens.components.NotifyBanner
import com.mobile.android_task.utils.RequestStoragePermission
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import com.mobile.android_task.viewmodel.NetworkViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaGalleryScreen(navController: NavController, folderId : String, folderName : String,fileViewModel: FileViewModel = viewModel(), viewModel: NetworkViewModel = hiltViewModel(),mediaGalleryViewModel: MediaGalleryViewModel){


    val fileDataListLocal by fileViewModel.fileDataListLocal.observeAsState(emptyList())  // Local BD

    val fileDataList by fileViewModel.fileDataLiveDataCloud.observeAsState(emptyList()) // cloud storage

    val isConnected by viewModel.isConnected

    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    var showDialogDownloadFile by remember { mutableStateOf(false) }

    val context = LocalContext.current

    var progress by remember { mutableStateOf(0) }

    val downloader = AndroidDownloader(context)

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val infoFileData = mediaGalleryViewModel.infoFileData.observeAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showPasteDialog by remember { mutableStateOf(false) }

    var showCopyDialog by remember { mutableStateOf(false) }


    LaunchedEffect (Unit){
        fileViewModel.getFileDataLocal(folderId)
        fileViewModel.getFileDataCloud(folderId)
    }

    Box(modifier = Modifier.fillMaxSize()){


        if (showCopyDialog && isConnected){
            AlertDialog(
                onDismissRequest = { /* Prevent dismissal */ },
                title = { Text(text = "Are you sure ?",  fontFamily = gilroy) },
                text = { Text(text = "If you press the Copy All Files are copy in the clipboard", fontFamily = gilroy) },

                confirmButton = {
                    Text(modifier = Modifier.
                    clickable {
                        mediaGalleryViewModel.copyFolder(folderId, onSuccess = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Files copied")
                            }
                        }
                        )
                        showCopyDialog = !showCopyDialog
                    }, text = "Copy",  fontFamily = gilroy)
                },
                dismissButton = {
                    Text(modifier = Modifier.
                    clickable {
                        showCopyDialog = !showCopyDialog
                    }, text = "Cancel",  fontFamily = gilroy)
                }
            )
        }

        if (showPasteDialog && isConnected){
            AlertDialog(
                onDismissRequest = { /* Prevent dismissal */ },
                title = { Text(text = "Are you sure ?",  fontFamily = gilroy) },
                text = { Text(text = "If you press the Paste All Copied Files are transfer to this Folder", fontFamily = gilroy) },

                confirmButton = {
                    Text(modifier = Modifier.
                    clickable {
                        coroutineScope.launch {
                            mediaGalleryViewModel.pasteFolder(
                                folderId,
                                isSuccess = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("File are Transferred")
                                    }
                                },
                                isFailure = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("File are Transferred Failed")
                                    }
                                }
                            )
                        }
                        showPasteDialog = !showPasteDialog
                    }, text = "Paste",  fontFamily = gilroy)
                },
                dismissButton = {
                    Text(modifier = Modifier.
                    clickable {
                        showPasteDialog = !showPasteDialog
                    }, text = "Cancel",  fontFamily = gilroy)
                }
            )
        }

        if (showDeleteDialog && isConnected){
            AlertDialog(
                onDismissRequest = { /* Prevent dismissal */ },
                title = { Text(text = "Are you sure ?",  fontFamily = gilroy) },
                text = { Text(text = "If you press the delete Button All files in this folder has been delete !!!.", fontFamily = gilroy) },

                confirmButton = {
                    Text(modifier = Modifier.
                    clickable {
                        mediaGalleryViewModel.deleteAllFilesFromFolder(
                            folderId,
                            onSuccess = {
                                Toast.makeText(context,"Files Deleted Successfully",Toast.LENGTH_SHORT).show()
                            },
                            onFailure = { Toast.makeText(context,"Files Deleted Failed",Toast.LENGTH_SHORT).show() },
                        )
                        showDeleteDialog = !showDeleteDialog
                    }, text = "Delete",  fontFamily = gilroy)
                },
                dismissButton = {
                    Text(modifier = Modifier.
                    clickable {
                        showDeleteDialog = !showDeleteDialog
                    }, text = "Cancel",  fontFamily = gilroy)
                }
            )
        }

        if (showBottomSheet){
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = {
                    showBottomSheet = false
                }
            ){
                Column (modifier = Modifier.fillMaxSize()
                    .padding(vertical = 15.dp, horizontal = 15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    ){

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .background(color = SkyBlue, shape = RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center,
                    ){


                        if (infoFileData.value?.determineFileType() == FileType.VIDEO){
                            infoFileData.value?.fileUrl?.let {
                                VideoPlayerScreen(it)
                            }
                        }
                        else{
                            AsyncImage(
                                modifier = Modifier.fillMaxWidth()
                                    .height(250.dp),
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data("${infoFileData.value?.fileUrl}")
                                    .crossfade(true)
                                    .placeholder(when(infoFileData.value?.determineFileType()){
                                        FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                        FileType.VIDEO -> R.drawable.baseline_video_file_24
                                        FileType.IMAGE -> R.drawable.baseline_image_24
                                        FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                                        else -> R.drawable.baseline_add_24
                                    })
                                    .error(when(infoFileData.value?.determineFileType()){
                                        FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                        FileType.VIDEO -> R.drawable.baseline_video_file_24
                                        FileType.IMAGE -> R.drawable.baseline_image_24
                                        FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                                        else -> R.drawable.baseline_add_24
                                    })
                                    .build(),
                                contentDescription = "Example Image"
                            )
                        }
                    }


                    Spacer(Modifier.height(10.dp))


                    Row (Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                        ){
                        Text(
                            "File name - >",
                            modifier = Modifier.width(140.dp)
                            .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "${infoFileData.value?.fileName}",
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )
                    }

                    Spacer(Modifier.height(10.dp))


                    Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            "File Type - >",
                            modifier = Modifier.width(140.dp)
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "${infoFileData.value?.fileType}",
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )
                    }

                    Spacer(Modifier.height(10.dp))


                    Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            "Upload date - >",
                            modifier = Modifier.width(140.dp)
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "${infoFileData.value?.createdDate}",
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )
                    }

                    Spacer(Modifier.height(10.dp))


                    Row (Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween){
                        Text(
                            "File Size - >",
                            modifier = Modifier.width(140.dp)
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )

                        Spacer(Modifier.width(10.dp))

                        Text(
                            "${infoFileData.value?.fileSize}",
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            style = TextStyle(
                                fontSize = 15.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )
                    }

                }
            }
        }

        if (showDialogDownloadFile){
            Dialog(onDismissRequest = {showDialogDownloadFile = !showDialogDownloadFile}) {
                Card(modifier = Modifier.fillMaxWidth()
                    .height(100.dp)) {
                    Column (modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ){

                        LinearProgressIndicator(progress = progress / 100f)

                        Text("Progress: $progress%")

                    }
                }
            }
        }


        Column (modifier = Modifier.fillMaxSize()){

            Box(modifier = Modifier
                .fillMaxWidth()
                .background(color = NavyBlue)
                .padding(horizontal = 15.dp)
                .height(60.dp),
                contentAlignment = Alignment.CenterStart,
            ){

                Row (verticalAlignment = Alignment.CenterVertically){


                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            tint = Color.White,
                            contentDescription = null,
                            painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        )

                    }

                    Text(
                        "${folderName}",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = gilroy,
                            fontWeight = FontWeight.W600
                        ),
                    )
                }
            }


            if (isConnected) {

                if (fileDataList == null){
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.Center)
                                .padding(16.dp),
                            strokeWidth = 5.dp,
                            color = SkyBlue,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
                else if (fileDataList.isEmpty()){
                    NotifyBanner("No Files are in this Folder,If you needed Copy in another folder and paste it in this folder.")
                }
                else{
                    fileDataList.let {
                        LazyColumn (modifier = Modifier.fillMaxSize()){
                            items(fileDataList.size) {index->
                                MediaItemFile(fileDataList.get(index),folderId,snackbarHostState,mediaGalleryViewModel, onShowDialogDownload = { downloadUrl,fileName ->
                                    coroutineScope.launch {
                                        downloader.downloadFile(downloadUrl,fileName)
                                    }
                                },
                                    onInfo = { showBottomSheet = !showBottomSheet },
                                )
                            }
                        }
                    }
                }
            }
            if (!isConnected){
                if (fileDataListLocal == null){
                    Box(modifier = Modifier.fillMaxSize()){
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.Center)
                                .padding(16.dp),
                            strokeWidth = 5.dp,
                            color = SkyBlue,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
                else if (fileDataListLocal.isEmpty()){
                    NotifyBanner("No Files are in this Folder,If you needed Copy in another folder and paste it in this folder.")
                }
                else{
                    fileDataListLocal.let {
                        LazyColumn (modifier = Modifier.fillMaxSize()){
                            items(fileDataListLocal.size) {index->
                                MediaItemFile(fileDataListLocal.get(index),folderId,snackbarHostState,mediaGalleryViewModel, onShowDialogDownload = { downloadUrl,fileName ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("You're in Offline")
                                    }
                                },
                                    onInfo = { showBottomSheet = !showBottomSheet }
                                )
                            }
                        }
                    }
                }
            }
        }


        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 15.dp, horizontal = 15.dp)
            .align(Alignment.BottomCenter)
            .height(60.dp)
            .background(color = NavyBlue, shape = RoundedCornerShape(30.dp))
        ){
            Row (modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
            ){



                Column (
                    modifier = Modifier.clickable {
                        if (isConnected){
                            navController.navigate("${AppConstants.UPLOAD_SCREEN_ROUTE}/$folderId")
                        }else{
                            Toast.makeText(context,"No Internet !! Oops !!!",Toast.LENGTH_SHORT).show()
                        }

                    },
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Icon(
                        tint = Color.White,
                        contentDescription = null,
                        painter = painterResource(R.drawable.baseline_upload_file_24),
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        "Upload",
                        style = TextStyle(
                            fontFamily = gilroy,
                            color = Color.White,
                            fontSize = 12.sp,
                        )
                    )
                }

                Column (modifier = Modifier.clickable {
                    if (isConnected){
                        showCopyDialog = !showCopyDialog
                    }else{
                        Toast.makeText(context,"No Internet !! Oops !!!",Toast.LENGTH_SHORT).show()
                    }

                },verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Icon(
                        tint = Color.White,
                        contentDescription = null,
                        painter = painterResource(R.drawable.baseline_copy_all_24),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "Copy",
                        style = TextStyle(
                            fontFamily = gilroy,
                            color = Color.White,
                            fontSize = 12.sp,
                        )
                    )
                }


                Column (modifier = Modifier.clickable {
                    if (isConnected){
                        showPasteDialog = !showPasteDialog
                    }else{
                        Toast.makeText(context,"No Internet !! Oops !!!",Toast.LENGTH_SHORT).show()
                    }

                },verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Icon(
                        tint = Color.White,
                        contentDescription = null,
                        painter = painterResource(R.drawable.baseline_content_paste_24),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "Paste",
                        style = TextStyle(
                            fontFamily = gilroy,
                            color = Color.White,
                            fontSize = 12.sp,
                        )
                    )
                }


                Column (modifier = Modifier.clickable {
                    if (isConnected){
                        coroutineScope.launch {
                            showDeleteDialog = !showDeleteDialog
                        }
                    }else{
                        Toast.makeText(context,"No Internet !! Oops !!!",Toast.LENGTH_SHORT).show()
                    }

                },verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Icon(
                        tint = Color.White,
                        contentDescription = null,
                        painter = painterResource(R.drawable.baseline_delete_24),
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        "Delete",
                        style = TextStyle(
                            fontFamily = gilroy,
                            color = Color.White,
                            fontSize = 12.sp,
                        )
                    )
                }

            }
        }


        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

    }

}


@Composable
fun VideoPlayerScreen(videoUrl: String) {
    val context = LocalContext.current

    // Create ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.Builder()
                .setUri(videoUrl)
                .build()
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }


    // Ensure the player is released when the composable is removed
    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    // Video player UI
    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = exoPlayer
            }
        },
        modifier = Modifier.background(color = SkyBlue, shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            .height(250.dp)

    )
}



@Composable
fun MediaItemFile(fileData: FileData,folderId: String,snackbarHostState: SnackbarHostState,mediaGalleryViewModel: MediaGalleryViewModel,onShowDialogDownload : (String,String)-> Unit,onInfo: ()-> Unit,networkViewModel: NetworkViewModel = hiltViewModel()){

    var expanded by remember { mutableStateOf(false) }

    val options = listOf("Copy","Remove","Download","Info")

    var selectedOption by remember { mutableStateOf("Select Item") }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val isConnected by networkViewModel.isConnected


    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 5.dp)
        .background(SkyBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp))
    ){

        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            ){



            Row (verticalAlignment = Alignment.CenterVertically){

                Box(modifier = Modifier
                    .size(50.dp)
                    .background(color = SkyBlue, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ){

                    Icon(
                        modifier = Modifier.size(30.dp),
                        contentDescription = null,
                        tint = Color.White,
                        painter = painterResource(
                            when(fileData.determineFileType()){
                                FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                FileType.VIDEO -> R.drawable.baseline_video_file_24
                                FileType.IMAGE -> R.drawable.baseline_image_24
                                FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                            }
                        )
                    )

                }

                Spacer(modifier = Modifier.width(10.dp))

                Column (
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ){

                    Text(
                        "${fileData.fileName}",
                        modifier = Modifier.width(200.dp),
                        maxLines = 1,
                        minLines = 1,
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.W800,
                        )
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        "${fileData.fileSize}",
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W400,
                        )
                    )

                }
            }


            Row {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(
                            leadingIcon = {
                                Icon(
                                    contentDescription = null,
                                    painter = when (option){
                                        "Copy" -> painterResource(R.drawable.baseline_copy_all_24)
                                        "Remove" -> painterResource(R.drawable.baseline_delete_24)
                                        "Info" -> painterResource(R.drawable.baseline_info_24)
                                        "Download" -> painterResource(R.drawable.baseline_file_download_24)
                                        else -> {painterResource(R.drawable.baseline_keyboard_arrow_down_24)}
                                    },
                                )
                            },
                            text = { Text(option) },
                            onClick = {
                                if (option.equals("Copy") && isConnected){
                                    coroutineScope.launch {
                                        mediaGalleryViewModel.copySingleFile(folderId,fileData.fileId, onSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("File Copied")
                                            }
                                        })
                                    }
                                }

                                if (option.equals("Remove") && isConnected){
                                    coroutineScope.launch {
                                        mediaGalleryViewModel.deleteSingleFile(
                                            folderId,
                                            fileData.fileId,
                                            onSuccess = { coroutineScope.launch { snackbarHostState.showSnackbar("File Removed") } },
                                            onFailure = { coroutineScope.launch { snackbarHostState.showSnackbar("File Removed Failed") } },
                                        )
                                    }
                                }

                                if (option.equals("Download") && isConnected){
                                    onShowDialogDownload(fileData.fileUrl,fileData.fileName)
                                }
                                if (option.equals("Info") && isConnected){
                                    coroutineScope.launch {
                                        mediaGalleryViewModel.infoFileData(fileData)
                                        onInfo()
                                    }
                                }
                                else{
                                    Toast.makeText(context,"No Internet !! Oops !!!",Toast.LENGTH_SHORT).show()
                                }
                                expanded = false
                            }
                        )
                    }
                }

                IconButton(
                    onClick = {
                        expanded = !expanded
                    },
                ) {

                    Icon(
                        modifier = Modifier.size(20.dp),
                        contentDescription = null,
                        tint = Color.Black,
                        painter = painterResource(R.drawable.ic_menu),
                    )

                }
            }




        }

    }
}


@Composable
fun ConfirmDialog() {

}


@Preview(showBackground = true)
@Composable
fun MediaGalleryScreenPreview(){
    val navController = rememberNavController()
    val mediaGalleryViewModel = MediaGalleryViewModel()
    AndroidTaskTheme {
        MediaGalleryScreen(navController,"","",mediaGalleryViewModel = mediaGalleryViewModel)
    }
}
