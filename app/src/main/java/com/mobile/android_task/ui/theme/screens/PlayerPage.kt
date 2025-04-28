package com.mobile.android_task.ui.theme.screens

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.android_task.Greeting
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.Blue
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.gilroy
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerPage(fileData: FileData,navController: NavController){


    var showBottomSheet by remember { mutableStateOf(false) }

    var showDialogCreateFolder by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    Box(modifier = Modifier.fillMaxSize()){

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
                            "${fileData?.fileName}",
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
                            "${fileData.fileType}",
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
                            "${fileData.createdDate}",
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
                            "${fileData.fileSize}",
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




        Column (modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){


            Box(modifier = Modifier
                .fillMaxWidth()
                .background(color = NavyBlue)
                .padding(horizontal = 15.dp, vertical = 15.dp)
                .height(30.dp),
                contentAlignment = Alignment.CenterStart,
            ){

                Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween){


                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {

                        Icon(
                            tint = Color.White,
                            contentDescription = null,
                            painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        )

                    }


                    IconButton(onClick = {
                        showBottomSheet = true
                    }) {

                        Icon(painter = painterResource(R.drawable.baseline_info_24), contentDescription = "", tint = Color.White)


                    }

                }
            }



            Spacer(modifier = Modifier.height(10.dp))





            Box(modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
                .background(color = Color.White, shape = RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center,
            ){


                if (fileData.determineFileType() == FileType.VIDEO){
                    VideoPlayerScreen(fileData.fileUrl)
                }

                else{
                    AsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("${fileData.fileUrl}")
                            .crossfade(true)
                            .placeholder(when(fileData.determineFileType()){
                                FileType.AUDIO -> R.drawable.baseline_audiotrack_24
                                FileType.VIDEO -> R.drawable.baseline_video_file_24
                                FileType.IMAGE -> R.drawable.baseline_image_24
                                FileType.DOCUMENT -> R.drawable.baseline_insert_drive_file_24
                            })
                            .error(when(fileData.determineFileType()){
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
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidTaskTheme {
        val navController = rememberNavController()
        PlayerPage(FileData(), navController = navController)
    }
}