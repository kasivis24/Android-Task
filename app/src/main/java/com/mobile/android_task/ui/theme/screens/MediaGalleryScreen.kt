package com.mobile.android_task.ui.theme.screens

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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobile.android_task.R
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy

@Composable
fun MediaGalleryScreen(navController: NavController){


    Box(modifier = Modifier.fillMaxSize()){



        Column (modifier = Modifier.fillMaxSize()){

            Box(modifier = Modifier.fillMaxWidth()
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
                        "Your Folder",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 18.sp,
                            fontFamily = gilroy,
                            fontWeight = FontWeight.W600
                        ),
                    )
                }
            }


            LazyColumn (modifier = Modifier.fillMaxSize()){

                items(20) {
                    MediaItem()
                }
            }

        }




        Box(modifier = Modifier.fillMaxWidth()
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
                        navController.navigate(AppConstants.UPLOAD_SCREEN_ROUTE)
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

                Column (verticalArrangement = Arrangement.Center,
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


                Column (verticalArrangement = Arrangement.Center,
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


                Column (verticalArrangement = Arrangement.Center,
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

    }

}


@Composable
fun MediaItem(){
    Box(modifier = Modifier.fillMaxWidth()
        .padding(horizontal = 15.dp, vertical = 5.dp)
        .background(SkyBlue.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp))
    ){

        Row (modifier = Modifier.fillMaxWidth()
            .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            ){



            Row (verticalAlignment = Alignment.CenterVertically){

                Box(modifier = Modifier.size(50.dp)
                    .background(color = SkyBlue, shape = RoundedCornerShape(10.dp)),
                    contentAlignment = Alignment.Center,
                ){

                    Icon(
                        modifier = Modifier.size(30.dp),
                        contentDescription = null,
                        tint = Color.White,
                        painter = painterResource(R.drawable.baseline_folder_24),
                    )

                }

                Spacer(modifier = Modifier.width(10.dp))

                Column (
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                ){

                    Text(
                        "Document 1",
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


            IconButton(
                onClick = {},
            ) {

                Icon(
                    modifier = Modifier.size(20.dp),
                    contentDescription = null,
                    painter = painterResource(R.drawable.baseline_arrow_forward_ios_24),
                )

            }



        }

    }
}


@Preview(showBackground = true)
@Composable
fun MediaGalleryScreenPreview(){
    val navController = rememberNavController()
    AndroidTaskTheme {
        MediaGalleryScreen(navController)
    }
}
