package com.mobile.android_task.ui.theme.screens

import android.Manifest
import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.mobile.android_task.R
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.CardBlue
import com.mobile.android_task.ui.theme.Green
import com.mobile.android_task.ui.theme.NavyBlue
import com.mobile.android_task.ui.theme.Red
import com.mobile.android_task.ui.theme.SkyBlue
import com.mobile.android_task.ui.theme.Yellow
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.ui.theme.screens.components.NotifyBanner
import com.mobile.android_task.utils.SpeechRecognizerHelper
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.FolderViewModel
import com.mobile.android_task.viewmodel.NetworkViewModel
import com.mobile.android_task.viewmodel.SearchViewModel
import kotlinx.coroutines.launch

//
//@Composable
//fun MyComponent(content: @Composable (String) -> Unit) {
//    Column {
//        Text("Above the custom content")
//        content() // Calling the composable lambda
//        Text("Below the custom content")
//    }
//}
//
//
//@Composable
//fun UsageExample() {
//    MyComponent {
//        Text("This is the passed content!")
//        Text("This is the passed content!")
//    }
//}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController,searchViewModel: SearchViewModel = viewModel(),viewModel: NetworkViewModel = hiltViewModel()){


    val searchQuery = searchViewModel.searchQuery

    val coroutineScope = rememberCoroutineScope()

    val folderList by searchViewModel.filteredFolderList.observeAsState(emptyList())

    val colors = listOf(Yellow, CardBlue, Green, Red)

    val folderViewModel = FolderViewModel()

    var showDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    val context = LocalContext.current

    val speechHelper = remember { mutableStateOf<SpeechRecognizerHelper?>(null) }

    val lifecycleOwner = LocalLifecycleOwner.current

    val isConnected by viewModel.isConnected

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                speechHelper.value?.destroy()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            speechHelper.value?.destroy()
        }
    }


    Box(modifier = Modifier.fillMaxSize()){


        if (showDialog) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxSize(),
                sheetState = sheetState,
                onDismissRequest = {
                    showDialog = !showDialog
                }
            ){

                Column (
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ){
                    Image(
                        alignment = Alignment.Center,
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(R.drawable.mic_gif) // Replace with your GIF file
                                .build()
                        ),
                        contentDescription = "GIF Image"
                    )

                    Text("Speak to search songs",
                        fontFamily = gilroy,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center,
                    )
                }

            }
        }



        Column (modifier = Modifier.fillMaxSize()
            ){


            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = gilroy,
                ),
                placeholder = {
                    Text("Search Here",
                        fontFamily = gilroy,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center,
                    )
                },
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 15.dp,),
                value = searchQuery.value,
                trailingIcon = {
                    Box(modifier = Modifier){
                        IconButton(onClick = {

                                if (isConnected){
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        ActivityCompat.requestPermissions(
                                            (context as android.app.Activity),
                                            arrayOf(Manifest.permission.RECORD_AUDIO),
                                            1,
                                        )
                                        Log.d("Log","Per rec")
                                        showDialog = !showDialog
                                    }
                                    speechHelper.value = SpeechRecognizerHelper(context,
                                        onResult = { result ->
                                            searchViewModel.searchQuery.value = result
                                            searchViewModel.updateFilteredFolder()
                                            Log.d("Log","on result")
                                            showDialog = !showDialog
                                        },
                                        onError = { error ->
                                            showDialog = !showDialog
                                            Toast.makeText(context, "Speak again", Toast.LENGTH_SHORT).show()
                                        })
                                    speechHelper.value?.startListening()
                                }else{
                                    Toast.makeText(context,"No Internet !! Oops !!!",Toast.LENGTH_SHORT).show()
                                }
                        }) {

                            Image(
                                modifier = Modifier.size(23.dp),
                                painter = painterResource(R.drawable.baseline_mic_24),
                                colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inverseOnSurface),
                                contentDescription = null,
                            )
                        }
                    }
                },
                leadingIcon = {
                    Box(modifier = Modifier){
                        Image(
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inverseOnSurface),
                            painter = painterResource(id = R.drawable.ic_search,
                            ),
                            contentDescription = "ic_search",
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                },

                onValueChange = {
                    coroutineScope.launch {
                        searchQuery.value = it
                        searchViewModel.updateFilteredFolder()
                    }
                },
                shape = RoundedCornerShape(13.dp),
                singleLine = true,
                maxLines = 1,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Gray.copy(alpha = 0.5f),
                    disabledBorderColor = Color.Gray.copy(alpha = 0.5f),
                ),

            )





            if (folderList == null){
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

            if (folderList.isEmpty()){
                NotifyBanner("No folders are created. Please create the folder and upload your files.")
            }
            else{
                folderList?.let {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 15.dp, vertical = 10.dp)
                    ) {



                        items(it.size) { item ->

                            val containerColor = colors[item % colors.size]

                            val folderId = folderList.get(item).folderId

                            val folderName = folderList.get(item).folderName

                            val optionsFolderMenu = listOf("Delete", "Info")

                            val coroutineScope = rememberCoroutineScope()

                            var expandedFolderMenu by remember { mutableStateOf(false) }

                            Card(
                                onClick = {
                                    navController.navigate("${AppConstants.MEDIA_SCREEN_ROUTE}/$folderId/$folderName")
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


                                        Row {

                                            DropdownMenu(
                                                expanded = expandedFolderMenu,
                                                onDismissRequest = { expandedFolderMenu = false }
                                            ) {
                                                optionsFolderMenu.forEach { option ->
                                                    DropdownMenuItem(
                                                        leadingIcon = {
                                                            Icon(
                                                                contentDescription = null,
                                                                painter = if (option.equals("Delete")) painterResource(R.drawable.baseline_delete_24) else painterResource(R.drawable.baseline_info_24),
                                                            )
                                                        },
                                                        text = { Text(option) },
                                                        onClick = {
                                                            if (option.equals("Delete") && isConnected) {
                                                                coroutineScope.launch {
                                                                    folderViewModel.deleteFolderAndItsFile(folderId)
                                                                    Toast.makeText(context,"Folder Deleted",
                                                                        Toast.LENGTH_SHORT).show()
                                                                }
                                                            }else {
                                                                Toast.makeText(context,"No Internet!! Oops !!!",Toast.LENGTH_SHORT).show()
                                                            }
                                                            expandedFolderMenu = false
                                                        }
                                                    )
                                                }
                                            }

                                            Icon(
                                                modifier = Modifier.size(20.dp)
                                                    .clickable {
                                                        expandedFolderMenu = !expandedFolderMenu
                                                    },
                                                tint = containerColor,
                                                painter = painterResource(R.drawable.ic_menu),
                                                contentDescription = null,
                                            )
                                        }




                                    }


                                    Text(
                                        "${folderList.get(item).folderName}",
                                        modifier = Modifier.padding(vertical = 10.dp),
                                        style = TextStyle(
                                            color = containerColor,
                                            fontFamily = gilroy,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.W900,
                                        )
                                    )

                                    Text(
                                        "${folderList.get(item).createdDate}",
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
    }




}

@Composable
fun SearchList(searchViewModel: SearchViewModel = viewModel(), navController: NavController){



}


@Preview(showBackground = true)
@Composable
fun SearchScreenPreview(){
    AndroidTaskTheme {
        val navController = rememberNavController()
        SearchScreen(navController = navController)
    }
}