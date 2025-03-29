package com.mobile.android_task.ui.theme.navigation
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.screens.DashboardPage
import com.mobile.android_task.ui.theme.screens.LoginPage
import com.mobile.android_task.ui.theme.screens.MediaGalleryScreen
import com.mobile.android_task.ui.theme.screens.MediaItem
import com.mobile.android_task.ui.theme.screens.SignUpPage
import com.mobile.android_task.ui.theme.screens.UploadScreen
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import com.mobile.apicalljetcompose.ui.theme.screens.SplashScreen

@Composable
fun AppNavigation(navHostController: NavHostController,mediaGalleryViewModel: MediaGalleryViewModel){
    NavHost(navController = navHostController, startDestination = AppConstants.SPLASH_SCREEN_ROUTE){
        composable (AppConstants.SPLASH_SCREEN_ROUTE) { SplashScreen(navHostController) }
        composable (AppConstants.SIGNUP_SCREEN_ROUTE) { SignUpPage(navHostController) }
        composable (AppConstants.LOGIN_SCREEN_ROUTE) { LoginPage(navHostController) }
        composable (AppConstants.DASHBOARD_SCREEN_ROUTE) { DashboardPage(navHostController,mediaGalleryViewModel) }
        composable("${AppConstants.UPLOAD_SCREEN_ROUTE}/{folderId}",
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType },
            )
        ){ UploadScreen(navHostController, folderId = it.arguments?.getString("folderId") ?: "",mediaGalleryViewModel) }

        composable("${AppConstants.MEDIA_SCREEN_ROUTE}/{folderId}/{folderName}",
            arguments = listOf(
                navArgument("folderId") { type = NavType.StringType },
                navArgument("folderName") { type = NavType.StringType },
            )
        ){ MediaGalleryScreen(navHostController, folderId = it.arguments?.getString("folderId") ?: "", folderName = it.arguments?.getString("folderName") ?: "",mediaGalleryViewModel = mediaGalleryViewModel) } }

    }