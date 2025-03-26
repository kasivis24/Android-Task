package com.mobile.android_task.ui.theme.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.mobile.android_task.ui.theme.gilroy

@Composable
fun UploadScreen(navController: NavController){
    Box(modifier = Modifier.fillMaxSize()){


        Box(modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 15.dp)
            .align(Alignment.BottomCenter)
            .background(color = NavyBlue, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center,
        ){

            Text(
                "Upload",
                modifier = Modifier.padding(vertical = 17.dp),
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


        }
    }
}


@Preview(showBackground = true)
@Composable
fun UploadScreenPreview(){
    val navController = rememberNavController()
    AndroidTaskTheme { 
        UploadScreen(navController)
    }
}