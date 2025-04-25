package com.mobile.android_task.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.local.FileType
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.gilroy

@Composable
fun PlayerPage(fileData: FileData){


    Box(modifier = Modifier.fillMaxSize()){
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


                if (fileData.determineFileType() == FileType.VIDEO){
                    VideoPlayerScreen(fileData.fileUrl)
                }
                else{
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth()
                            .height(250.dp),
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
                    "${fileData.fileName}",
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
                    "${fileData?.fileType}",
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
                    "${fileData?.fileSize}",
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