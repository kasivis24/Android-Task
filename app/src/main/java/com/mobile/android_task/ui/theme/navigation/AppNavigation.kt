package com.mobile.android_task.ui.theme.navigation

import MainScreen
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
import com.mobile.android_task.ui.theme.screens.SignUpPage
import com.mobile.android_task.ui.theme.screens.UploadScreen

@Composable
fun AppNavigation(navHostController: NavHostController,){
    NavHost(navController = navHostController, startDestination = AppConstants.DASHBOARD_SCREEN_ROUTE){
        composable (AppConstants.SIGNUP_SCREEN_ROUTE) { SignUpPage(navHostController) }
        composable (AppConstants.LOGIN_SCREEN_ROUTE) { LoginPage(navHostController) }
        composable (AppConstants.DASHBOARD_SCREEN_ROUTE) { DashboardPage(navHostController) }
        composable (AppConstants.MEDIA_SCREEN_ROUTE) { MediaGalleryScreen(navHostController) }
        composable (AppConstants.UPLOAD_SCREEN_ROUTE){ UploadScreen(navHostController) }
    }
}