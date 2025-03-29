package com.mobile.apicalljetcompose.ui.theme.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.firebase.auth.FirebaseAuth
import com.mobile.android_task.R
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {


    val systemUiController = rememberSystemUiController()

    val auth = FirebaseAuth.getInstance()

    // Set status bar color
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false,
        )
    }

    // Using LaunchedEffect to navigate after delay
    LaunchedEffect(Unit) {
        delay(2000) // Delay for 2 seconds

        if (auth.currentUser != null){
            navController.navigate(AppConstants.DASHBOARD_SCREEN_ROUTE) {
                popUpTo(AppConstants.SPLASH_SCREEN_ROUTE) { inclusive = true }
            }
        }else{
            navController.navigate(AppConstants.SIGNUP_SCREEN_ROUTE) {
                popUpTo(AppConstants.SPLASH_SCREEN_ROUTE) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo), // Replace with your logo
            contentDescription = "Splash Logo",
            modifier = Modifier.size(200.dp)
                .align(Alignment.Center)
        )

        Text(modifier = Modifier.align(Alignment.BottomCenter), text = "ZoundBox", fontSize = 20.sp, fontFamily = gilroy,fontWeight = FontWeight.W500, color = Color.Gray.copy(alpha = 0.3f))


    }
}


