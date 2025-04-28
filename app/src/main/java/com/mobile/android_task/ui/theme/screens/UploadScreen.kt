package com.mobile.android_task.ui.theme.screens

import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import kotlinx.coroutines.launch
import kotlin.math.min

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UploadScreen(navController: NavController,folderId : String,mediaGalleryViewModel: MediaGalleryViewModel){



    val context = LocalContext.current

    val fileViewModel = FileViewModel()

    var filesSelectList = remember { mutableStateListOf<FileData>() }


    var isProgress by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    val coroutineScope = rememberCoroutineScope()

    var showDialogCreateFolder by remember { mutableStateOf(false) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        filesSelectList.clear()
        filesSelectList.addAll(uris.map {
            fileViewModel.getFileMetadata(context,it,"${folderId}")
        })
        Log.d("Log","list ${filesSelectList}")
    }



    Box(modifier = Modifier.fillMaxSize()){


        if (showDialogCreateFolder){
            Dialog(
                onDismissRequest = {  },
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ){


                    Box(modifier = Modifier.fillMaxSize().padding(15.dp)){
                        Column (modifier = Modifier.fillMaxSize()){
                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "Files Uploaded Successfully !",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W900,
                                fontFamily = gilroy
                            )

                            Text(
                                modifier = Modifier.padding(vertical = 5.dp),
                                text = "Are you want to upload more files ?",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.W600,
                                fontFamily = gilroy
                            )


                            Spacer(Modifier.height(10.dp))

                            Row (modifier = Modifier.fillMaxSize()){
                                Button(onClick = {
                                    showDialogCreateFolder = !showDialogCreateFolder
                                    navController.popBackStack()
                                },Modifier.padding(horizontal = 5.dp),
                                    colors = ButtonColors(containerColor = SkyBlue, contentColor = Color.White, disabledContentColor = SkyBlue, disabledContainerColor = Color.White)
                                    ) {
                                    Text("Dismiss", color = Color.White)
                                }
                                Button(onClick = {
                                    filesSelectList.clear()
                                    showDialogCreateFolder = !showDialogCreateFolder
                                },
                                    colors = ButtonColors(containerColor = SkyBlue, contentColor = Color.White, disabledContentColor = SkyBlue, disabledContainerColor = Color.White)
                                ) {
                                    Text("Continue", color = Color.White)
                                }
                            }
                        }
                    }




                }

            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 16.dp)
        )

        Box(modifier = Modifier.fillMaxWidth()
            .height(60.dp)
            .clickable {

                var count = 0;
                if (filesSelectList.isNotEmpty()){
                    isProgress = !isProgress
                    coroutineScope.launch {
                        fileViewModel.uploadTheFiles(filesSelectList,folderId,
                            isSuccess = {
                                count += 1
                                if (count == filesSelectList.size){
                                    coroutineScope.launch {
                                        isProgress = !isProgress
                                        snackbarHostState.showSnackbar("Files are uploaded succesfully")
                                        showDialogCreateFolder = !showDialogCreateFolder
                                    }
                                }
                        },
                            isFailure = {
                                coroutineScope.launch {
                                    isProgress = !isProgress
                                    snackbarHostState.showSnackbar("Something went wrong")
                                }
                        }
                        )
                    }
                }else{
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("No Files is selected")
                    }
                }
            }
            .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
            .align(Alignment.BottomCenter)
            .background(color = NavyBlue, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ){

            if (isProgress)
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    color = Color.White,
                    strokeWidth = 2.5.dp,
                )
            else
                Text(
                    "Upload",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 18.sp,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.W600
                    ),
                )

        }

        Column (modifier = Modifier.fillMaxSize()){


            Box(modifier = Modifier.fillMaxWidth()
                .clickable {
                    filePickerLauncher.launch("*/*")
                }
                .padding(vertical = 15.dp, horizontal = 15.dp)
                .border(border = BorderStroke(width = 2.dp, color = NavyBlue), shape = RoundedCornerShape(15.dp),)){

                Column (modifier = Modifier
                    .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    ){


                    Spacer(modifier = Modifier.height(5.dp))


                    Box(modifier = Modifier.background(color = NavyBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(30.dp))){
                        Icon(
                            modifier = Modifier.padding(15.dp),
                            contentDescription = null,
                            painter = painterResource(R.drawable.baseline_upload_file_24)
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))


                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ){
                        Text(
                            "Click here",
                            style = TextStyle(
                                color = SkyBlue,
                                fontSize = 16.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W600
                            ),
                        )

                        Text(
                            " to upload your file",
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W400
                            ),
                        )
                    }

                    Spacer(modifier = Modifier.height(5.dp))


                    Text(
                        "SupportedFileFormat (All kind of files format)",
                        style = TextStyle(
                            color = Color.Gray.copy(alpha = 0.4f),
                            fontSize = 14.sp,
                            fontFamily = gilroy,
                            fontWeight = FontWeight.W500
                        ),
                    )

                }
            }


            filesSelectList.let { list ->

                LazyColumn (modifier = Modifier.fillMaxSize().padding(bottom = 80.dp)){
                    items (list.size){
                        selectedFilesItem(list.get(it),{ fileData ->
                            filesSelectList.remove(fileData)
                        })
                    }
                }

            }





        }
    }
}


@Composable
fun selectedFilesItem(filedata : FileData,onRemove : (FileData) -> Unit){
    Box(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 5.dp)
        .background(color = Color.Gray.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp))
        ){


        Row (modifier = Modifier.fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            ){

            Row (verticalAlignment = Alignment.CenterVertically){


                Box(modifier = Modifier.background(color = NavyBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(30.dp))){
                    Icon(
                        modifier = Modifier.padding(15.dp),
                        contentDescription = null,
                        painter = painterResource(
                            when(filedata.determineFileType()){
                                FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                FileType.VIDEO -> R.drawable.baseline_video_file_24
                                FileType.IMAGE -> R.drawable.baseline_image_24
                                FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                            }
                        )
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                Column (modifier = Modifier.width(200.dp)
                    .height(40.dp),
                    verticalArrangement = Arrangement.Center,
                ){

                    Text(
                        "${filedata.fileName}",
                        maxLines = 1,
                        minLines = 1,
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W600,
                        )
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        "${filedata.fileSize} / ${filedata.fileType}",
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontSize = 13.sp,
                        )
                    )



                }

            }


            IconButton(onClick = {
                onRemove(filedata)
            }) {

                Icon(
                    contentDescription = null,
                    painter = painterResource(R.drawable.baseline_cancel_24),
                )

            }

        }

    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun UploadScreenPreview(){
    val navController = rememberNavController()
    val mediaGalleryViewModel = MediaGalleryViewModel()
    AndroidTaskTheme { 
        UploadScreen(navController,"",mediaGalleryViewModel)
    }
}