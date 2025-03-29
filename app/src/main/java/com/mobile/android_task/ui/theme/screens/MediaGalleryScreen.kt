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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import com.mobile.android_task.viewmodel.NetworkViewModel
import kotlinx.coroutines.launch

@Composable
fun MediaGalleryScreen(navController: NavController, folderId : String, folderName : String,fileViewModel: FileViewModel = viewModel(), viewModel: NetworkViewModel = hiltViewModel(),mediaGalleryViewModel: MediaGalleryViewModel){


    val fileDataListLocal = fileViewModel.fileDataListLocal.observeAsState(emptyList())  // Local BD

    val fileDataList = fileViewModel.fileDataLiveDataCloud.observeAsState(emptyList()) // cloud storage

    val isConnected by viewModel.isConnected

    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }


    LaunchedEffect (Unit){
        fileViewModel.getFileDataLocal(folderId)
        fileViewModel.getFileDataCloud(folderId)
    }

    Box(modifier = Modifier.fillMaxSize()){




        Column (modifier = Modifier.fillMaxSize()){

            Box(modifier = Modifier
                .fillMaxWidth()
                .background(color = NavyBlue)
                .padding(horizontal = 15.dp)
                .height(60.dp),
                contentAlignment = Alignment.CenterStart,
            ){

                Row (verticalAlignment = Alignment.CenterVertically){


                    IconButton(onClick = {}) {

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
                fileDataList.let {
                    LazyColumn (modifier = Modifier.fillMaxSize()){
                        items(fileDataList.value.size) {index->
                            MediaItem(fileDataList.value.get(index),folderId,snackbarHostState,mediaGalleryViewModel)
                        }
                    }
                }
            }
            else{
                fileDataListLocal.let {
                    LazyColumn (modifier = Modifier.fillMaxSize()){
                        items(fileDataListLocal.value.size) {index->
                            MediaItem(fileDataListLocal.value.get(index),folderId,snackbarHostState,mediaGalleryViewModel)
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
                        navController.navigate("${AppConstants.UPLOAD_SCREEN_ROUTE}/$folderId")
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
                    mediaGalleryViewModel.copyFolder(folderId, onSuccess = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Files copied")
                        }
                    })
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
                    coroutineScope.launch {
                        mediaGalleryViewModel.deleteAllFilesFromFolder(
                            folderId,
                            onSuccess = { },
                            onFailure = { },
                        )
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
fun MediaItem(fileData: FileData,folderId: String,snackbarHostState: SnackbarHostState,mediaGalleryViewModel: MediaGalleryViewModel){

    var expanded by remember { mutableStateOf(false) }

    val options = listOf("Copy","Remove","Info")

    var selectedOption by remember { mutableStateOf("Select Item") }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current


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
                        "17 items",
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
                                        else -> {painterResource(R.drawable.baseline_keyboard_arrow_down_24)}
                                    },
                                )
                            },
                            text = { Text(option) },
                            onClick = {
                                if (option.equals("Copy")){
                                    coroutineScope.launch {
                                        mediaGalleryViewModel.copySingleFile(folderId,fileData.fileId, onSuccess = {
                                            coroutineScope.launch {
                                                snackbarHostState.showSnackbar("File Copied")
                                            }
                                        })
                                    }
                                }

                                if (option.equals("Remove")){
                                    coroutineScope.launch {
                                        mediaGalleryViewModel.deleteSingleFile(
                                            folderId,
                                            fileData.fileId,
                                            onSuccess = { coroutineScope.launch { snackbarHostState.showSnackbar("File Removed") } },
                                            onFailure = { coroutineScope.launch { snackbarHostState.showSnackbar("File Removed Failed") } },
                                        )
                                    }
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


@Preview(showBackground = true)
@Composable
fun MediaGalleryScreenPreview(){
    val navController = rememberNavController()
    val mediaGalleryViewModel = MediaGalleryViewModel()
    AndroidTaskTheme {
        MediaGalleryScreen(navController,"","",mediaGalleryViewModel = mediaGalleryViewModel)
    }
}
