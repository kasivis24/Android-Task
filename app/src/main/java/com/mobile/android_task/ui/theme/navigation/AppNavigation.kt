package com.mobile.android_task.ui.theme.navigation
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.screens.DashboardPage
import com.mobile.android_task.ui.theme.screens.LoginPage
import com.mobile.android_task.ui.theme.screens.MediaGalleryScreen
import com.mobile.android_task.ui.theme.screens.PlayerPage
import com.mobile.android_task.ui.theme.screens.SearchScreen
import com.mobile.android_task.ui.theme.screens.SignUpPage
import com.mobile.android_task.ui.theme.screens.SplashScreen
import com.mobile.android_task.ui.theme.screens.ThemePreferenceManager
import com.mobile.android_task.ui.theme.screens.UploadScreen
import com.mobile.android_task.viewmodel.MediaGalleryViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(isDark : Boolean,onChange : () -> Unit,themePreferenceManager: ThemePreferenceManager,navHostController: NavHostController,mediaGalleryViewModel: MediaGalleryViewModel){
    NavHost(navController = navHostController, startDestination = AppConstants.SPLASH_SCREEN_ROUTE){
        composable (AppConstants.SPLASH_SCREEN_ROUTE) { SplashScreen(navHostController) }
        composable (AppConstants.SIGNUP_SCREEN_ROUTE) { SignUpPage(navHostController) }
        composable (AppConstants.LOGIN_SCREEN_ROUTE) { LoginPage(navHostController) }
        composable (AppConstants.SEARCH_SCREEN_ROUTE) { SearchScreen(navHostController) }
        composable (AppConstants.DASHBOARD_SCREEN_ROUTE) { DashboardPage(navHostController,isDark,onChange,themePreferenceManager,mediaGalleryViewModel) }
        composable("${AppConstants.UPLOAD_SCREEN_ROUTE}/{folderId}",
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType },
            )
        ){ UploadScreen(navHostController, folderId = it.arguments?.getString("folderId") ?: "",mediaGalleryViewModel) }

        composable("${AppConstants.PLAYER_SCREEN_ROUTE}/{fileDataJson}",
            arguments = listOf(
                navArgument("fileDataJson") { type = NavType.StringType },
            )
        ){
            val fileDataJson = it.arguments?.getString("fileDataJson")
            val fileData = Gson().fromJson(fileDataJson, FileData::class.java)
            PlayerPage(fileData,navHostController)
        }

        composable("${AppConstants.MEDIA_SCREEN_ROUTE}/{folderId}/{folderName}",
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType },
                navArgument("folderName") { type = NavType.StringType },
            )
        ){ MediaGalleryScreen(navHostController, folderId = it.arguments?.getString("folderId") ?: "", folderName = it.arguments?.getString("folderName") ?: "",mediaGalleryViewModel = mediaGalleryViewModel) } }

    }