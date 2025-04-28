package com.mobile.android_task.ui.theme.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.data.entities.PieChartData
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
import com.mobile.android_task.ui.theme.screens.components.NotifyBanner
import com.mobile.android_task.viewmodel.AuthViewModel
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.FolderViewModel
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import com.mobile.android_task.viewmodel.NetworkViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random
import com.mobile.android_task.ui.theme.screens.PieChart as PieChart


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ContextCastToActivity")
@Composable
fun DashboardPage(navController: NavController,isDark : Boolean,onChangeTheme: () -> Unit,themePreferenceManager: ThemePreferenceManager,mediaGalleryViewModel: MediaGalleryViewModel,viewModel: NetworkViewModel = hiltViewModel()){

    var showDialogCreateFolder by remember { mutableStateOf(false) }
    
    var folderName by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    val folderViewModel = FolderViewModel()

    val snackbarHostState = remember { SnackbarHostState() }

    var isProgress by remember { mutableStateOf(false) }

    val authViewModel = AuthViewModel()

    var expanded by remember { mutableStateOf(false) }

    val options = listOf("Storage","Theme","Logout")

    val sheetState = rememberModalBottomSheetState( skipPartiallyExpanded = false )

    var showBottomSheet by remember { mutableStateOf(false) }

    val sheetStatePieChart = rememberModalBottomSheetState( skipPartiallyExpanded = false )

    var showLogoutDialog by remember { mutableStateOf(false) }

    var showBottomSheetPieChart by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val isConnected by viewModel.isConnected

    val fileViewModel = FileViewModel()

    var exitDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val activity = LocalContext.current as? Activity


    BackHandler {
        exitDialog = !exitDialog
    }


    Scaffold (modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
            onClick = {

                if (isConnected){
                    showDialogCreateFolder = !showDialogCreateFolder
                }else{
                    Toast.makeText(context,"No Internet!! Oops !!!",Toast.LENGTH_SHORT).show()
                }
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
        Box(modifier = Modifier.fillMaxSize()
            .padding(innerPadding)
        ){


            if (exitDialog){
                AlertDialog(
                    onDismissRequest = { exitDialog = !exitDialog },
                    title = {
                        Text(
                            text = "Are you sure exit the app ?",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.W600,
                            fontFamily = gilroy
                        )
                    },
                    text = {

                    },
                    dismissButton = {
                        Text(
                            modifier = Modifier.clickable {
                                activity?.finish()
                                exitDialog = !exitDialog
                            },
                            text = "yes",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = gilroy
                        )
                    },
                    confirmButton = {
                        Text(
                            modifier = Modifier.clickable {
                                exitDialog = !exitDialog
                            },
                            text = "no",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.W400,
                            fontFamily = gilroy
                        )
                    }
                )
            }

            if (showLogoutDialog && isConnected){
                AlertDialog(
                    onDismissRequest = { /* Prevent dismissal */ },
                    title = { Text(text = "Are you sure ?",  fontFamily = gilroy) },
                    text = { Text(text = "Logout the Account in this device.", fontFamily = gilroy) },

                    confirmButton = {
                        Text(modifier = Modifier.
                        clickable {
                            if (isConnected){
                                coroutineScope.launch {
                                    authViewModel.logout(fileViewModel,folderViewModel)
                                    navController.navigate(AppConstants.LOGIN_SCREEN_ROUTE){
                                        popUpTo(AppConstants.DASHBOARD_SCREEN_ROUTE) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }else{
                                Toast.makeText(context,"No Internet!! Oops !!!",Toast.LENGTH_SHORT).show()
                            }
                            showLogoutDialog = !showLogoutDialog
                        }, text = "Logout",  fontFamily = gilroy)
                    },
                    dismissButton = {
                        Text(modifier = Modifier.
                        clickable {
                            showLogoutDialog = !showLogoutDialog
                        }, text = "Cancel",  fontFamily = gilroy)
                    }
                )
            }

            if (showBottomSheetPieChart){
                ModalBottomSheet(
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    sheetState = sheetStatePieChart,
                    onDismissRequest = {
                        showBottomSheetPieChart = !showBottomSheetPieChart
                    }
                ){
                    Column (modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        ){

                        PieChart()

                    }
                }
            }

            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = Modifier.fillMaxWidth()
                        .fillMaxHeight(0.5f),
                    sheetState = sheetState,
                    onDismissRequest = {
                        showBottomSheet = !showBottomSheet
                    }
                ){


                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                            Icon(
                                painter = if (isDark) painterResource(R.drawable.baseline_dark_mode_24) else painterResource(R.drawable.baseline_light_mode_24),
                                contentDescription = "Dark Mode Icon",
                                tint = MaterialTheme.colorScheme.inverseOnSurface,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = if (isDark) "Dark Mode" else "Light Mode", fontSize = 20.sp, fontFamily = gilroy)
                            Spacer(modifier = Modifier.height(16.dp))
                            Switch(
                                checked = isDark,
                                onCheckedChange = { newValue ->
                                    scope.launch {
                                        onChangeTheme()
                                        themePreferenceManager.saveTheme(newValue)
//                                    ProcessPhoenix.triggerRebirth(context)
                                    }
                                }
                            )
                        }
                    }


                }
            }


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
                                    .background(color = Blue, shape = RoundedCornerShape(15.dp))
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
                                .padding(vertical = 15.dp, horizontal = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){

                            Text(
                                "Your Zpace",
                                style = TextStyle(
                                    fontSize = 25.sp,
                                    fontFamily = gilroy,
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    fontWeight = FontWeight.W600
                                ),
                            )


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
                                                    painter = when(option){
                                                        "Storage" -> painterResource(R.drawable.baseline_storage_24)
                                                        "Theme" -> painterResource(R.drawable.baseline_color_lens_24)
                                                        else -> painterResource(R.drawable.baseline_logout_24)
                                                    },
                                                )
                                            },
                                            text = { Text(option) },
                                            onClick = {
                                                if (option.equals("Logout") && isConnected) {
                                                    showLogoutDialog = !showLogoutDialog
                                                    expanded = false;
                                                }

                                                if (option.equals("Theme")) {
                                                    coroutineScope.launch {
                                                        showBottomSheet = !showBottomSheet
                                                        expanded = false
                                                    }
                                                }

                                                if (option.equals("Storage")) {
                                                    coroutineScope.launch {
                                                        mediaGalleryViewModel.loadPieChartData()
                                                        showBottomSheetPieChart = !showBottomSheetPieChart
                                                        expanded = false
                                                    }
                                                }


                                            }
                                        )
                                    }
                                }



                                Image(
                                    colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.primaryContainer,),
                                    modifier = Modifier.size(25.dp)
                                        .clickable {
                                            expanded = !expanded
                                        },
                                    painter = painterResource(R.drawable.ic_cate),
                                    contentDescription = null,
                                )
                            }







                        }




                Box(modifier = Modifier.fillMaxWidth()
                    .clickable {
                        navController.navigate(AppConstants.SEARCH_SCREEN_ROUTE)
                    }
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
                            colorFilter = ColorFilter.tint(color = MaterialTheme.colorScheme.inverseOnSurface),
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



@Composable
fun PieChart(mediaGalleryViewModel: MediaGalleryViewModel = viewModel(),) {


    val context = LocalContext.current
    val pieCharList by mediaGalleryViewModel.pieChartData.observeAsState(emptyList())

    if (pieCharList == null){

        CircularProgressIndicator(
        modifier = Modifier
            .size(70.dp)
            .padding(16.dp),
        strokeWidth = 5.dp,
        color = SkyBlue,
        strokeCap = StrokeCap.Round
    )
    }
    if (pieCharList.isEmpty()){
        NotifyBanner("No Files are created So no memory used")
    }
    else{
        Column(modifier = Modifier.fillMaxSize()) {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Text(
                    text = "Cloud and Local Storage Used",

                    style = TextStyle.Default,

                    fontFamily = FontFamily.Default,

                    fontStyle = FontStyle.Normal,

                    fontSize = 20.sp
                )

                Column(
                    modifier = Modifier
                        .padding(18.dp)
                        .size(320.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Crossfade(targetState = pieCharList) { pieChartData ->

                        AndroidView(factory = { context ->

                            PieChart(context).apply {
                                layoutParams = LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                )

                                this.description.isEnabled = false

                                this.isDrawHoleEnabled = false

                                this.legend.isEnabled = true

                                this.legend.textSize = 14F

                                this.legend.horizontalAlignment =
                                    Legend.LegendHorizontalAlignment.CENTER

                                ContextCompat.getColor(context, R.color.white)

                            }
                        },

                            modifier = Modifier
                                .wrapContentSize()
                                .padding(5.dp), update = {
                                updatePieChartWithData(it, pieChartData,context)
                            })
                    }
                }
            }
        }
    }
}


fun updatePieChartWithData(
    chart: PieChart,
    data: List<PieChartData>,
    context: Context,
) {

    val entries = ArrayList<PieEntry>()
    for (i in data.indices) {
        val item = data[i]
        entries.add(PieEntry(item.value ?: 0.toFloat(), item.name ?: ""))
    }

    val ds = PieDataSet(entries, "")


    ds.colors = arrayListOf(
        Yellow.toArgb(),
        Blue.toArgb(),
        Red.toArgb(),
        Green.toArgb(),
    )

    ds.yValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    ds.xValuePosition = PieDataSet.ValuePosition.INSIDE_SLICE

    ds.sliceSpace = 2f


    ContextCompat.getColor(context, R.color.white)

    ds.valueTextSize = 18f

    ds.valueTypeface = Typeface.DEFAULT_BOLD

    val d = PieData(ds)
    chart.data = d

    chart.invalidate()
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GridList(navController: NavController,viewModel: NetworkViewModel = hiltViewModel(),folderViewModel: FolderViewModel = viewModel()) {



    val colors = listOf(Yellow, CardBlue, Green, Red)

    val folderList by folderViewModel.folderLiveData.observeAsState(emptyList())

    val folderLocalList by folderViewModel.folderLocalLiveData.observeAsState(emptyList())

    val isConnected by viewModel.isConnected

    val context = LocalContext.current




    if (folderList == null && isConnected){
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

    if (folderList.isEmpty() && isConnected){
        NotifyBanner("No folders are created. Please create the folder and upload your files.")
    }

    if (!folderList.isEmpty() &&  isConnected){

        folderList?.let {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
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

                                                    if (!isConnected){
                                                        Toast.makeText(context,"No Internet! Oops !!!",Toast.LENGTH_SHORT).show()
                                                    }

                                                    if (option.equals("Delete") && isConnected) {
                                                        coroutineScope.launch {
                                                            folderViewModel.deleteFolderAndItsFile(folderId)
                                                            Toast.makeText(context,"Folder Deleted",Toast.LENGTH_SHORT).show()
                                                        }
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



    /// Without Network

    if (folderLocalList == null && !isConnected){
        Log.d("Log","Local DB Without Intenet No data")
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

    if (folderLocalList.isEmpty() && !isConnected){
        NotifyBanner("No folders are created. Please create the folder and upload your files.")
    }

    if (!folderLocalList.isEmpty() && !isConnected){

        folderLocalList.let {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 15.dp)
            ) {



                items(it.size) { item ->

                    val containerColor = colors[item % colors.size]

                    val folderId = folderLocalList.get(item).folderId

                    val folderName = folderLocalList.get(item).folderName

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

                                Icon(
                                    modifier = Modifier.size(20.dp),
                                    tint = containerColor,
                                    painter = painterResource(R.drawable.ic_menu),
                                    contentDescription = null,
                                )


                            }


                            Text(
                                "${folderLocalList.get(item).folderName}",
                                modifier = Modifier.padding(vertical = 10.dp),
                                style = TextStyle(
                                    color = containerColor,
                                    fontFamily = gilroy,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.W900,
                                )
                            )

                            Text(
                                "${folderLocalList.get(item).createdDate}",
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
    val mediaGalleryViewModel = MediaGalleryViewModel()
    val context = LocalContext.current
    val themePreferenceManager = ThemePreferenceManager(context)
    AndroidTaskTheme {
        DashboardPage(navController, true, onChangeTheme = {},themePreferenceManager = themePreferenceManager,mediaGalleryViewModel = mediaGalleryViewModel)
    }
}

