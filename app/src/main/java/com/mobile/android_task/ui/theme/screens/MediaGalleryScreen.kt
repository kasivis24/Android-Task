package com.mobile.android_task.ui.theme.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.mobile.android_task.AndroidDownloader
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.AndroidTaskTheme
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

@SuppressLint("ContextCastToActivity")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MediaGalleryScreen(navController: NavController, folderId : String, folderName : String, fileViewModel: FileViewModel = viewModel(), viewModel: NetworkViewModel = hiltViewModel(), mediaGalleryViewModel: MediaGalleryViewModel,folderViewModel: FolderViewModel = viewModel()) {


    val windowSizeClass = calculateWindowSizeClass(activity = LocalContext.current as Activity)

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            Log.d("Log","Compact")
            CompactMediaScreen(navController, folderId, folderName, fileViewModel, viewModel, mediaGalleryViewModel, folderViewModel)
        }

        WindowWidthSizeClass.Medium -> {
            Log.d("Log","Medium")
            MediumMediaScreen(navController, folderId, folderName, fileViewModel, viewModel, mediaGalleryViewModel, folderViewModel)
        }

        WindowWidthSizeClass.Expanded -> {
            Log.d("Log","Expanded")
            MediumMediaScreen(navController, folderId, folderName, fileViewModel, viewModel, mediaGalleryViewModel, folderViewModel)

        }

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



@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MediaItemFile(fileData: FileData,folderId: String,snackbarHostState: SnackbarHostState,mediaGalleryViewModel: MediaGalleryViewModel,onShowDialogDownload : (String,String)-> Unit,onInfo: ()-> Unit,networkViewModel: NetworkViewModel = hiltViewModel(),selectionFolder: (FileData)-> Unit,onClick: (String) -> Unit,navController: NavController,changeView : ()-> Unit,viewData : (FileData)-> Unit,orentaion : Boolean){

    var expanded by remember { mutableStateOf(false) }

    val options = listOf("Copy to","Remove","Download","Info")

    var selectedOption by remember { mutableStateOf("Select Item") }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val isConnected by networkViewModel.isConnected




    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 5.dp)
        .background(SkyBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp))
        .clickable {

            if (orentaion){
                viewData(fileData)
                changeView()
            }else{
                val fileDataJson = Uri.encode(Gson().toJson(fileData))
                navController.navigate("${AppConstants.PLAYER_SCREEN_ROUTE}/$fileDataJson")
            }
        }
    ){

        Column (Modifier.fillMaxSize()){
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
                            "${fileData.uploadedTime}",
                            style = TextStyle(
                                fontFamily = gilroy,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.W400,
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
                                            "Copy to" -> painterResource(R.drawable.baseline_copy_all_24)
                                            "Remove" -> painterResource(R.drawable.baseline_delete_24)
                                            "Info" -> painterResource(R.drawable.baseline_info_24)
                                            "Download" -> painterResource(R.drawable.baseline_file_download_24)
                                            else -> {painterResource(R.drawable.baseline_keyboard_arrow_down_24)}
                                        },
                                    )
                                },
                                text = { Text(option) },
                                onClick = {
                                    if (option.equals("Copy to") && isConnected){
                                        coroutineScope.launch {
                                            selectionFolder(fileData)
//                                        mediaGalleryViewModel.copySingleFile(folderId,fileData.fileId, onSuccess = {
//                                            coroutineScope.launch {
//                                                snackbarHostState.showSnackbar("File Copied")
//                                            }
//                                        })
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
//                                    coroutineScope.launch {
//                                        mediaGalleryViewModel.infoFileData(fileData)
////                                        onInfo()
//                                    }

                                    }

                                    if(!isConnected){
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
}

@Composable
fun ForFolderSelectionItem(folderData: FolderData,selectFolder: (String)-> Unit,onRemovePlaylist: (String)->Unit,list: List<String>){

    Box(modifier = Modifier
        .fillMaxWidth()
        .height(height = 76.dp)
        .padding(vertical = 7.dp)){

        Row{
            Box(modifier = Modifier
                .width(70.dp)
                .padding(horizontal = 8.dp, vertical = 3.dp)
                .height(70.dp)){

                Box(modifier = Modifier
                    .width(60.dp)
                    .align(Alignment.Center)
                    .background(
                        color = SkyBlue.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(14.dp),

                        )
                    .height(60.dp)){

                    Image(painter = painterResource(id = R.drawable.baseline_folder_24), contentDescription = "ic_folder",
                        modifier = Modifier
                            .align(alignment = Alignment.Center)
                            .width(30.dp)
                            .height(30.dp))

                }
            }

            Box(modifier = Modifier
                .fillMaxSize()){

                Row (modifier = Modifier.fillMaxSize(),horizontalArrangement = Arrangement.SpaceBetween,){
                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){
                        Text(modifier = Modifier.fillMaxWidth(0.5f), text = "${folderData.folderName}",maxLines = 1,  fontSize = 14.sp, fontFamily = gilroy, fontWeight = FontWeight.W400)
                        Text(text = "Created date : ${folderData.createdDate}", fontSize = 12.sp, fontFamily = gilroy,fontWeight = FontWeight.W400)
                    }


                    Column (modifier = Modifier.fillMaxHeight(),verticalArrangement = Arrangement.Center){

                        Row (horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically){
                            Box(modifier = Modifier
                                .fillMaxHeight()
                                .padding(vertical = 7.dp)){

                            }

                            if (list.contains(folderData.folderId)){
                                IconButton(onClick = {
                                    onRemovePlaylist(folderData.folderId)
                                }) {
                                    Icon(modifier = Modifier.size(25.dp), painter = painterResource(id = R.drawable.baseline_check_circle_24), contentDescription = "unselected",)
                                }
                            }else{
                                IconButton(onClick = {
                                    selectFolder(folderData.folderId)
                                }) {
                                    Icon(modifier = Modifier.size(25.dp), painter = painterResource(id = R.drawable.baseline_check_circle_outline_24), tint = Color.Gray.copy(alpha = 0.3f), contentDescription = "selected",)
                                }
                            }





                        }

                    }

                }

            }

        }

    }
}

