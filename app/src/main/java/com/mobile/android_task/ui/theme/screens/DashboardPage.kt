package com.mobile.android_task.ui.theme.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.android_task.R
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.Blue
import com.mobile.android_task.ui.theme.CardBlue
import com.mobile.android_task.ui.theme.Green
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.Red
import com.mobile.android_task.ui.theme.Yellow
import com.mobile.android_task.ui.theme.gilroy
import kotlin.random.Random


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardPage(){


    Scaffold (modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
            onClick = { },
            containerColor = NavyBlue,
            shape = RoundedCornerShape(30.dp),) {
                Icon(
                    tint = Color.White,
                    painter = painterResource(R.drawable.baseline_add_24),
                    contentDescription = null,
                )
        }},
        ){ innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)){
            Column (modifier = Modifier.fillMaxSize()
            ){

                        Row (
                            modifier = Modifier.fillMaxWidth()
                                .padding(vertical = 20.dp, horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){

                            Text(
                                "Your dribbbox",
                                style = TextStyle(
                                    fontSize = 25.sp,
                                    fontFamily = gilroy,
                                    fontWeight = FontWeight.W600
                                ),
                            )

                            Image(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(R.drawable.ic_cate),
                                contentDescription = null,
                            )


                        }


                        Box(modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 15.dp)
                            .height(55.dp)
                            .border(width = 1.5.dp, color = Color.Gray.copy(alpha = 0.3f), shape = RoundedCornerShape(15.dp)),
                            contentAlignment = Alignment.CenterStart,
                        )
                        {

                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ){

                                Image(
                                    modifier = Modifier.padding(horizontal = 20.dp)
                                        .size(20.dp),
                                    painter = painterResource(R.drawable.ic_search),
                                    contentDescription = null,
                                )

                                Text(
                                    "Search folder",
                                    style = TextStyle(
                                        fontFamily = gilroy,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                )
                            }

                        }


                        Row (
                            modifier = Modifier.fillMaxWidth()
                                .padding( horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ){


                            Row (
                                verticalAlignment = Alignment.CenterVertically,
                            ){
                                Text(
                                    "Recent",
                                    style = TextStyle(
                                        fontFamily = gilroy,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W600,
                                    )
                                )



                                IconButton(
                                    onClick = {
                                        //
                                    }) {
                                    Icon(
                                        painter = painterResource(R.drawable.baseline_keyboard_arrow_down_24),
                                        contentDescription = null,
                                    )
                                }


                            }



                            IconButton(
                                onClick = {
                                    //
                                }) {

                                Icon(
                                    painter = painterResource(R.drawable.baseline_filter_list_24),
                                    contentDescription = null,
                                )

                            }


                        }

                GridList()

            }
        }
    }
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridList() {

    val itemsList = (1..20).toList()

    val colors = listOf(Yellow, CardBlue, Green, Red)


    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 15.dp)
    ) {
        items(itemsList) { item ->

            val containerColor = colors[item % colors.size]

            Card(
                modifier = Modifier
                    .height(120.dp)
                    .padding(7.dp),
                colors = CardDefaults.elevatedCardColors(
                    disabledContainerColor = containerColor.copy(alpha = 0.2f),
                    containerColor = containerColor.copy(alpha = 0.2f),
                ),
            ) {

                Column (modifier = Modifier.fillMaxSize()
                    .padding(10.dp)){

                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ){

                        Icon(
                            modifier = Modifier.size(30.dp),
                            tint = containerColor,
                            painter = painterResource(R.drawable.baseline_folder_24),
                            contentDescription = null,
                        )

                        Icon(
                            modifier = Modifier.size(20.dp),
                            tint = containerColor,
                            painter = painterResource(R.drawable.ic_menu),
                            contentDescription = null,
                        )


                    }


                    Text(
                        "Folder 1",
                        modifier = Modifier.padding(vertical = 10.dp),
                        style = TextStyle(
                            color = containerColor,
                            fontFamily = gilroy,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W900,
                        )
                    )

                    Text(
                        "November 23-06-2025",
                        style = TextStyle(
                            color = containerColor,
                            fontFamily = gilroy,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.W500,
                        )
                    )
                }
            }
        }
    }

}




@Preview(showBackground = true)
@Composable
fun DashBoardPreview(){
    AndroidTaskTheme {
        DashboardPage()
    }
}

