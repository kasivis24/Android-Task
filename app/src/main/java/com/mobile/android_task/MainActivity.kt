package com.mobile.android_task
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.mobile.android_task.data.local.AppDatabase
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.navigation.AppNavigation
import com.mobile.android_task.ui.theme.screens.LoginPage
import com.mobile.android_task.ui.theme.screens.MediaGalleryScreen
import com.mobile.android_task.ui.theme.screens.SignUpPage
import com.mobile.android_task.ui.theme.screens.ThemePreferenceManager
import com.mobile.android_task.viewmodel.MediaGalleryViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    lateinit var themePreferenceManager: ThemePreferenceManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        themePreferenceManager = ThemePreferenceManager(this)

        setContent {

            var isDark by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                themePreferenceManager.themeFlow.collect { savedTheme ->
                    isDark = savedTheme
                }
            }
            val navController = rememberNavController()

            val mediaGalleryViewModel : MediaGalleryViewModel = viewModel()


            val context = LocalContext.current
            val lifecycleOwner = LocalLifecycleOwner.current




            AndroidTaskTheme (darkTheme = isDark){

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHostState() },
                    ) { innerPadding ->

                    DisposableEffect (lifecycleOwner){
                        
                        onDispose {

                        }
                    }

                    Box(modifier = Modifier.padding(innerPadding)){
                        AppNavigation(isDark, onChange = {isDark = !isDark}, themePreferenceManager,navController,mediaGalleryViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidTaskTheme {
        Greeting("Android")
    }
}