package com.mobile.android_task.ui.theme.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.Blue
import com.mobile.android_task.ui.theme.CardBlue
import com.mobile.android_task.ui.theme.Green
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.Red
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.Yellow
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.viewmodel.FolderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DashboardPage(navController: NavController){


    var showDialogCreateFolder by remember { mutableStateOf(false) }
    
    var folderName by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val folderViewModel = FolderViewModel()

    val snackbarHostState = remember { SnackbarHostState() }

    var isProgress by remember { mutableStateOf(false) }






    Scaffold (modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
            onClick = {
                showDialogCreateFolder = !showDialogCreateFolder
            },
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




            if (showDialogCreateFolder){
                Dialog(onDismissRequest = { showDialogCreateFolder = !showDialogCreateFolder }) {


                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                    ){


                        Box(modifier = Modifier.fillMaxSize()
                        ){


                            Column (modifier = Modifier.fillMaxSize()){
                                Text(
                                    "Create Folder",
                                    modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
                                    style = TextStyle(
                                        fontSize = 18.sp,
                                        fontFamily = gilroy,
                                        fontWeight = FontWeight.W600
                                    ),
                                )

                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 15.dp, horizontal = 15.dp)
                                ){

                                    OutlinedTextField(
                                        textStyle = TextStyle(
                                            fontFamily = gilroy,
                                            fontSize = 14.sp,
                                        ),
                                        placeholder = {
                                            Text("Enter the folder name",
                                                fontSize = 13.sp,
                                                fontFamily = gilroy,
                                                color = Color.Gray.copy(alpha = 0.7f),
                                                fontWeight = FontWeight.W500,
                                                textAlign = TextAlign.Center,
                                            )
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                            .height(50.dp),
                                        value = folderName,
                                        onValueChange = {
                                            folderName = it
                                        },
                                        shape = RoundedCornerShape(30.dp),
                                        singleLine = true,
                                        maxLines = 1,
                                        colors = OutlinedTextFieldDefaults.colors(
//                            focusedContainerColor = Color.Gray.copy(alpha = 0.5f),
                                            focusedBorderColor = Color.Gray.copy(alpha = 0.5f),
//                            disabledContainerColor = Color.Gray.copy(alpha = 0.5f),
                                            disabledBorderColor = Color.Gray.copy(alpha = 0.5f),
//                            cursorColor = if (isDark) AppThemeColor else Color.Red,
                                        ),

                                        )

                                }




                                Box(modifier = Modifier.padding(horizontal = 30.dp, vertical = 7.dp)
                                    .fillMaxWidth()
                                    .clickable {
                                        coroutineScope.launch {
                                            if (!folderName.isEmpty()) {

                                                isProgress = !isProgress
                                                folderViewModel.createFolder(
                                                    folderName,
                                                    onSuccess = {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Folder create successfully")
                                                            showDialogCreateFolder = !showDialogCreateFolder
                                                            isProgress = !isProgress
                                                            folderName = "";
                                                        }
                                                    },
                                                    onFailure = {
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar("Something Went Wrong")
                                                            showDialogCreateFolder = !showDialogCreateFolder
                                                            isProgress = !isProgress
                                                            folderName = "";
                                                        }
                                                    },
                                                )
                                            } else {
                                                snackbarHostState.showSnackbar("Folder name is required")
                                            }
                                        }
                                    }
                                    .background(color = SkyBlue, shape = RoundedCornerShape(15.dp))
                                    .height(45.dp),
                                    contentAlignment = Alignment.Center,
                                ){
                                    Log.d("Log", isProgress.toString())
                                    if (isProgress){

                                        CircularProgressIndicator(
                                            modifier = Modifier.size(18.dp),
                                            color = Color.White,
                                            strokeWidth = 2.5.dp,
                                        )
                                    }
                                    else{
                                        Text(
                                            "Create",
                                            modifier = Modifier.padding(horizontal = 30.dp),
                                            style = TextStyle(
                                                color = Color.White,
                                                fontSize = 15.sp,
                                                fontFamily = gilroy,
                                                fontWeight = FontWeight.W700
                                            ),
                                        )
                                    }
                                }

                            }

                        }


                    }

                }
            }

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
                                    color = NavyBlue,
                                    fontWeight = FontWeight.W600
                                ),
                            )

                            Image(
                                colorFilter = ColorFilter.tint(color = NavyBlue),
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

                GridList(navController)

            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = 16.dp)
            )

        }
    }
}







@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridList(navController: NavController) {



    val colors = listOf(Yellow, CardBlue, Green, Red)


    val folderList = FolderViewModel().folderLiveData.observeAsState(emptyList())


    if (folderList.value.isEmpty()){
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
    if (!folderList.value.isEmpty()){

        folderList.let {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) {



                items(it.value.size) { item ->

                    val containerColor = colors[item % colors.size]

                    Card(
                        onClick = {
                            navController.navigate(AppConstants.MEDIA_SCREEN_ROUTE)
                        },
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
                                "${folderList.value.get(item).folderName}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                style = TextStyle(
                                    color = containerColor,
                                    fontFamily = gilroy,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W900,
                                )
                            )

                            Text(
                                "${folderList.value.get(item).createdDate}",
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

    }





}




@Preview(showBackground = true)
@Composable
fun DashBoardPreview(){

    val navController = rememberNavController()
    AndroidTaskTheme {
        DashboardPage(navController)
    }
}

