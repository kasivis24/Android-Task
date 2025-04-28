package com.mobile.android_task.ui.theme.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.mobile.android_task.AndroidDownloader
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.Blue
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.ui.theme.screens.components.NotifyBanner
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.FolderViewModel
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import com.mobile.android_task.viewmodel.NetworkViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediumMediaScreen(navController: NavController, folderId : String, folderName : String, fileViewModel: FileViewModel = viewModel(), viewModel: NetworkViewModel = hiltViewModel(), mediaGalleryViewModel: MediaGalleryViewModel, folderViewModel: FolderViewModel = viewModel()){

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

    val viewFileData by mediaGalleryViewModel.selectedFileItem.collectAsState()

    val viewChange by mediaGalleryViewModel.isChangeView.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    var showPasteDialog by remember { mutableStateOf(false) }

    var showCopyDialog by remember { mutableStateOf(false) }

    var showBottomSheetSelectFolder by remember { mutableStateOf(false) }

    val sheetStateSelectFolder = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    var showDialogView by remember { mutableStateOf(false) }

    val fileData = remember { mutableStateOf(FileData(0,"","","","","","","","")) }

    val forSelectFolderList = remember { mutableStateListOf<String>() }

    val myFolderList by folderViewModel.folderLiveData.observeAsState(emptyList())

    val auth = FirebaseAuth.getInstance()




    LaunchedEffect (Unit){
        fileViewModel.getFileDataLocal(folderId)
        fileViewModel.getFileDataCloud(folderId)
    }


    Box(Modifier.fillMaxSize()){
        Row (Modifier.fillMaxSize()){
            Column (Modifier.weight(1f).padding(8.dp)){


                Box(modifier = Modifier.fillMaxSize()) {


        if (showDialogView){
            Dialog(
                onDismissRequest = { showDialogView = !showDialogView },
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ){


                    Box(modifier = Modifier.fillMaxWidth().padding(15.dp)){
                        Column (modifier = Modifier.fillMaxWidth()){


                            Box(Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ){

                                Text(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    text = "File Details",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W900,
                                    fontFamily = gilroy
                                )


                            }

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "File Name --> ${viewFileData.fileName}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = gilroy
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "File Type --> ${viewFileData.fileType}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = gilroy
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "Upload Date --> ${viewFileData.createdDate}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = gilroy
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "Upload Time --> ${viewFileData.uploadedTime}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = gilroy
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "File Size --> ${viewFileData.fileSize}",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = gilroy
                            )


                            Spacer(Modifier.height(10.dp))

                        }
                    }




                }

            }
        }

                    Row (modifier = Modifier.fillMaxSize()) {

                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {

                            if (showBottomSheetSelectFolder) {
                                ModalBottomSheet(
                                    modifier = Modifier.fillMaxSize(),
                                    sheetState = sheetStateSelectFolder,
                                    onDismissRequest = {
                                        forSelectFolderList.clear()
                                        showBottomSheetSelectFolder = false
                                    }
                                ) {

                                    Box(modifier = Modifier.fillMaxSize()) {

                                        Column(modifier = Modifier.fillMaxSize()) {


                                            Box(
                                                modifier = Modifier.fillMaxWidth()
                                                    .padding(horizontal = 10.dp)
                                                    .height(50.dp)
                                            ) {
                                                Row(
                                                    modifier = Modifier.fillMaxSize()
                                                        .align(Alignment.Center),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Row {
                                                        Text(
                                                            "Selected Folders - ${forSelectFolderList.size}",
                                                            fontFamily = gilroy
                                                        )
                                                    }
                                                    IconButton(
                                                        onClick = {
                                                            val auth = FirebaseAuth.getInstance()

                                                            val userId = auth.currentUser?.uid

                                                            coroutineScope.launch {
                                                                mediaGalleryViewModel.uploadSelectedFolder(userId.toString(),
                                                                    forSelectFolderList,
                                                                    fileData,
                                                                    isSuccess = {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Files Added to Folders",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    },
                                                                    isFailure = {
                                                                        Toast.makeText(
                                                                            context,
                                                                            "Files Added to Folders Failed",
                                                                            Toast.LENGTH_SHORT
                                                                        ).show()
                                                                    })
                                                                showBottomSheetSelectFolder =
                                                                    !showBottomSheetSelectFolder
                                                                forSelectFolderList.clear()
                                                            }

                                                        }, colors = IconButtonDefaults.iconButtonColors(
                                                            containerColor = Color.Gray.copy(alpha = 0.2f),
                                                            disabledContainerColor = Color.Gray.copy(alpha = 0.2f)
                                                        )
                                                    ) {
                                                        Icon(
                                                            painter = painterResource(R.drawable.baseline_check_24),
                                                            contentDescription = null
                                                        )
                                                    }
                                                }
                                            }

                                            Box(modifier = Modifier.padding(5.dp)) {
                                                Divider(
                                                    color = MaterialTheme.colorScheme.inverseSurface
                                                )
                                            }



                                            Box(modifier = Modifier.fillMaxSize()) {

                                                if (myFolderList == null) {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier
                                                            .size(70.dp)
                                                            .padding(16.dp),
                                                        color = Blue,
                                                        strokeWidth = 5.dp,
                                                        strokeCap = StrokeCap.Round
                                                    )
                                                }
                                                if (myFolderList.isEmpty()) {
                                                    NotifyBanner("Please Navigate to playlist page to create the playlist and Add the songs")
                                                }
                                                myFolderList?.let {
                                                    LazyColumn(modifier = Modifier.fillMaxSize()) {

                                                        items(myFolderList) { folderDataFromList ->
                                                            ForFolderSelectionItem(
                                                                folderDataFromList,
                                                                selectFolder = { forSelectFolderList.add(it) },
                                                                onRemovePlaylist = { forSelectFolderList.remove(it) },
                                                                forSelectFolderList
                                                            )
                                                        }

                                                    }
                                                }
                                            }
                                        }
                                    }


                                }
                            }

                            if (showCopyDialog && isConnected) {
                                AlertDialog(
                                    onDismissRequest = { /* Prevent dismissal */ },
                                    title = { Text(text = "Are you sure ?", fontFamily = gilroy) },
                                    text = {
                                        Text(
                                            text = "If you press the Copy All Files are copy in the clipboard",
                                            fontFamily = gilroy
                                        )
                                    },

                                    confirmButton = {
                                        Text(modifier = Modifier.clickable {
                                            mediaGalleryViewModel.copyFolder(folderId, onSuccess = {
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar("Files copied")
                                                }
                                            }
                                            )
                                            showCopyDialog = !showCopyDialog
                                        }, text = "Copy All", fontFamily = gilroy)
                                    },
                                    dismissButton = {
                                        Text(modifier = Modifier.clickable {
                                            showCopyDialog = !showCopyDialog
                                        }, text = "Cancel", fontFamily = gilroy)
                                    }
                                )
                            }

                            if (showPasteDialog && isConnected) {
                                AlertDialog(
                                    onDismissRequest = { /* Prevent dismissal */ },
                                    title = { Text(text = "Are you sure ?", fontFamily = gilroy) },
                                    text = {
                                        Text(
                                            text = "If you press the Paste All Copied Files are transfer to this Folder",
                                            fontFamily = gilroy
                                        )
                                    },

                                    confirmButton = {
                                        Text(modifier = Modifier.clickable {
                                            val authToken = auth.currentUser?.uid
                                            try {
                                                coroutineScope.launch(Dispatchers.IO) {
                                                    mediaGalleryViewModel.pasteFolder(
                                                        authToken.toString(),
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
                                                        },
                                                        error = {
                                                            Toast.makeText(context, "${it}", Toast.LENGTH_SHORT)
                                                                .show()
                                                        },
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                Toast.makeText(context, "Excep ${e}", Toast.LENGTH_SHORT).show()
                                            }
                                            showPasteDialog = !showPasteDialog
                                        }, text = "Paste", fontFamily = gilroy)
                                    },
                                    dismissButton = {
                                        Text(modifier = Modifier.clickable {
                                            showPasteDialog = !showPasteDialog
                                        }, text = "Cancel", fontFamily = gilroy)
                                    }
                                )
                            }

                            if (showDeleteDialog && isConnected) {
                                AlertDialog(
                                    onDismissRequest = { /* Prevent dismissal */ },
                                    title = { Text(text = "Are you sure ?", fontFamily = gilroy) },
                                    text = {
                                        Text(
                                            text = "If you press the delete Button All files in this folder has been delete !!!.",
                                            fontFamily = gilroy
                                        )
                                    },

                                    confirmButton = {
                                        Text(modifier = Modifier.clickable {
                                            mediaGalleryViewModel.deleteAllFilesFromFolder(
                                                folderId,
                                                onSuccess = {
                                                    Toast.makeText(
                                                        context,
                                                        "Files Deleted Successfully",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                                onFailure = {
                                                    Toast.makeText(
                                                        context,
                                                        "Files Deleted Failed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                },
                                            )
                                            showDeleteDialog = !showDeleteDialog
                                        }, text = "Delete", fontFamily = gilroy)
                                    },
                                    dismissButton = {
                                        Text(modifier = Modifier.clickable {
                                            showDeleteDialog = !showDeleteDialog
                                        }, text = "Cancel", fontFamily = gilroy)
                                    }
                                )
                            }



                            if (showDialogDownloadFile) {
                                Dialog(onDismissRequest = { showDialogDownloadFile = !showDialogDownloadFile }) {
                                    Card(
                                        modifier = Modifier.fillMaxWidth()
                                            .height(100.dp)
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            verticalArrangement = Arrangement.Center,
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {

                                            LinearProgressIndicator(progress = progress / 100f)

                                            Text("Progress: $progress%")

                                        }
                                    }
                                }
                            }


                            Column(modifier = Modifier.fillMaxSize()) {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(color = NavyBlue)
                                        .padding(horizontal = 15.dp)
                                        .height(60.dp),
                                    contentAlignment = Alignment.CenterStart,
                                ) {

                                    Row(verticalAlignment = Alignment.CenterVertically) {


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

                                    if (fileDataList == null) {
                                        Box(modifier = Modifier.fillMaxSize()) {
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
                                    } else if (fileDataList.isEmpty()) {
                                        NotifyBanner("No Files are in this Folder,If you needed Copy in another folder and paste it in this folder.")
                                    } else {
                                        fileDataList.let {
                                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                                items(fileDataList.size) { index ->

                                                    MediaItemFile(fileDataList.get(index),
                                                        folderId,
                                                        snackbarHostState,
                                                        mediaGalleryViewModel,
                                                        onShowDialogDownload = { downloadUrl, fileName ->
                                                            coroutineScope.launch {
                                                                downloader.downloadFile(downloadUrl, fileName)
                                                            }
                                                        },
                                                        onInfo = {
                                                            showBottomSheet = !showBottomSheet
                                                        },
                                                        selectionFolder = {
                                                            fileData.value = it
                                                            showBottomSheetSelectFolder =
                                                                !showBottomSheetSelectFolder
                                                        },
                                                        onClick = {},
                                                        navController = navController,
                                                        changeView = {
                                                            Log.d("Change", "View Chnaged")
                                                            mediaGalleryViewModel.changeView(!viewChange)
                                                        },
                                                        viewData = {
                                                            mediaGalleryViewModel.setFileData(it)
                                                            mediaGalleryViewModel.setItemId(it.id)
                                                        },
                                                        orentaion = true,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                if (!isConnected) {
                                    if (fileDataListLocal == null) {
                                        Box(modifier = Modifier.fillMaxSize()) {
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
                                    } else if (fileDataListLocal.isEmpty()) {
                                        NotifyBanner("No Files are in this Folder,If you needed Copy in another folder and paste it in this folder.")
                                    } else {
                                        fileDataListLocal.let {
                                            LazyColumn(modifier = Modifier.fillMaxSize()) {
                                                items(fileDataListLocal.size) { index ->

                                                    MediaItemFile(fileDataListLocal.get(index),
                                                        folderId,
                                                        snackbarHostState,
                                                        mediaGalleryViewModel,
                                                        onShowDialogDownload = { downloadUrl, fileName ->
                                                            coroutineScope.launch {
                                                                snackbarHostState.showSnackbar("You're in Offline")
                                                            }
                                                        },
                                                        onInfo = { showBottomSheet = !showBottomSheet },
                                                        selectionFolder = {
                                                            showBottomSheetSelectFolder =
                                                                !showBottomSheetSelectFolder
                                                        },
                                                        onClick = { navController.navigate("${AppConstants.PLAYER_SCREEN_ROUTE}/$folderId") },
                                                        navController = navController,
                                                        changeView = {},
                                                        viewData = {
                                                            mediaGalleryViewModel.setFileData(it)
                                                        },
                                                        orentaion = true,
                                                    )
                                                }
                                            }
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
                                        Toast.makeText(context,"No Internet !! Oops !!!", Toast.LENGTH_SHORT).show()
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
                                }
                                else{
                                    Toast.makeText(context,"No Internet !! Oops !!!", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(context,"No Internet !! Oops !!!", Toast.LENGTH_SHORT).show()
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
                                    Toast.makeText(context,"No Internet !! Oops !!!", Toast.LENGTH_SHORT).show()
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

            if(viewChange){
                Column (Modifier.weight(1f).padding(8.dp)){



                    Box(modifier = Modifier.fillMaxSize()){
                        Column (modifier = Modifier.fillMaxSize()){
                            Row (Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                                IconButton(onClick = {
                                    mediaGalleryViewModel.changeView(!viewChange)
                                }) {

                                    Icon(painter = painterResource(R.drawable.baseline_cancel_24), contentDescription = "")

                                }

                                IconButton(onClick = {
                                    showDialogView = !showDialogView
                                }) {

                                    Icon(painter = painterResource(R.drawable.baseline_info_outline_24), contentDescription = "")

                                }
                            }

                            Box(modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 15.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center,
                            ){


                                if (viewFileData.determineFileType() == FileType.VIDEO){
                                    VideoPlayerScreen(viewFileData.fileUrl)
                                }

                                else{
                                    AsyncImage(
                                        modifier = Modifier.fillMaxSize(),
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data("${viewFileData.fileUrl}")
                                            .crossfade(true)
                                            .placeholder(when(fileData.value.determineFileType()){
                                                FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                                FileType.VIDEO -> R.drawable.baseline_video_file_24
                                                FileType.IMAGE -> R.drawable.baseline_image_24
                                                FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                                            })
                                            .error(when(fileData.value.determineFileType()){
                                                FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                                FileType.VIDEO -> R.drawable.baseline_video_file_24
                                                FileType.IMAGE -> R.drawable.baseline_image_24
                                                FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                                            })
                                            .build(),
                                        contentDescription = "Image"
                                    )
                                }
                            }



                        }
                    }

                }
            }

        }
    }

}
