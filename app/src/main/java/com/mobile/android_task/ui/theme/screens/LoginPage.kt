package com.mobile.android_task.ui.theme.screens

import android.Manifest
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mobile.android_task.Greeting
import com.mobile.android_task.R
import com.mobile.android_task.data.entities.FileData
import com.mobile.android_task.data.entities.FolderData
import com.mobile.android_task.ui.theme.AndroidTaskTheme
import com.mobile.android_task.ui.theme.Blue
import com.mobile.android_task.ui.theme.Orange
import com.mobile.android_task.ui.theme.constants.AppConstants
import com.mobile.android_task.ui.theme.gilroy
import com.mobile.android_task.viewmodel.AuthViewModel
import com.mobile.android_task.viewmodel.FileViewModel
import com.mobile.android_task.viewmodel.FolderViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginPage(navController: NavController){


    var email by remember { mutableStateOf("") }

    var password by remember { mutableStateOf("") }

    var passwordSecure by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val snackbarHostState = remember { SnackbarHostState() }

    val authViewModel = AuthViewModel()

    var isProgress by remember { mutableStateOf(false) }

    val auth = FirebaseAuth.getInstance()

    val folderViewModel = FolderViewModel()
    val fileViewModel = FileViewModel()

    var dataListFolder by remember { mutableStateOf(emptyList<FolderData>()) }

    var dataListFile by remember { mutableStateOf(emptyList<FileData>()) }

    val uId = auth.currentUser?.uid



    Box(modifier = Modifier.fillMaxSize()){

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(top = 16.dp)
        )

        Column (
            modifier = Modifier.fillMaxSize()
            .padding(horizontal = 15.dp, vertical = 10.dp),
            ){


            Row (modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
                ){
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_back_ios_24),
                        contentDescription = null,
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text("Login",
                    style = TextStyle(
                        fontSize = 17.sp,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.W600,
                    ),
                )



            }

            Column (
                modifier = Modifier.fillMaxSize(),
                ){




                Column (modifier = Modifier.padding(vertical = 35.dp)){

                    Text(
                        "Welcome Back",
                        style = TextStyle(
                            fontFamily = gilroy,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.W600,
                        ),
                    )



                    Text(
                        "LOGIN IN TO YOUR ACCOUNT",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = gilroy,
                            fontWeight = FontWeight.W300,
                        ),
                        modifier = Modifier.padding(vertical = 20.dp),
                    )

                }




                Column {
                    Text(
                        "Email Address",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = gilroy,
                            fontWeight = FontWeight.W300,
                        ),
                    )



                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                    ){

                        OutlinedTextField(
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = gilroy
                            ),
                            placeholder = {
                                Text("guest@gmail.com",
                                    fontSize = 13.sp,
                                    fontFamily = gilroy,
                                    color = Color.Gray.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.W500,
                                    textAlign = TextAlign.Center,
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp),
                            value = email,
                            onValueChange = {
                                email = it
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


                }



                Column {
                    Text(
                        "Password",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontFamily = gilroy,
                            fontWeight = FontWeight.W300,
                        ),
                    )



                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp)
                    ){

                        OutlinedTextField(
                            textStyle = TextStyle(
                                fontFamily = gilroy,
                                fontSize = 14.sp,
                            ),
                            visualTransformation = if (passwordSecure) PasswordVisualTransformation() else VisualTransformation.None,
                            placeholder = {
                                Text("1234%$*&!",
                                    fontSize = 13.sp,
                                    fontFamily = gilroy,
                                    color = Color.Gray.copy(alpha = 0.7f),
                                    fontWeight = FontWeight.W500,
                                    textAlign = TextAlign.Center,
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        passwordSecure = !passwordSecure
                                    }) {
                                    Icon(
                                        if (passwordSecure) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                                        contentDescription = null,
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                                .height(50.dp),
                            value = password,
                            onValueChange = {
                                password = it
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


                }

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                ){

                    Text("Forget Password?",
                        fontSize = 14.sp,
                        color = Orange,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center,
                    )
                }


                Box(modifier = Modifier.padding(vertical = 15.dp)){

                    Box(modifier = Modifier.fillMaxWidth()
                        .clickable {
                            coroutineScope.launch {
                                authViewModel.signInWithEmail(
                                    email,password, state = {
                                        coroutineScope.launch {
                                            snackbarHostState.showSnackbar(it)
                                            isProgress = !isProgress
                                        }
                                    },
                                    eventState = { isState, uId->
                                        Log.d("Log","Event Success ${isState}")




                                        coroutineScope.launch {
                                            if (isState){
                                                Log.d("Log","coroutine ${uId}")
                                                coroutineScope.launch {
                                                    dataListFolder = folderViewModel.fetchDataFromFirestore(uId)
                                                    folderViewModel.resetDbFolder(dataListFolder)
                                                    Log.d("Log","DatalistFolder ${dataListFolder}")
                                                }
                                                coroutineScope.launch {
                                                    dataListFile = fileViewModel.fetchDataFromFireStoreFile(uId)
                                                    fileViewModel.resetDbFile(dataListFile)
                                                    Log.d("Log","DatalistFile ${dataListFolder}")
                                                }
                                                navController.navigate(AppConstants.DASHBOARD_SCREEN_ROUTE){
                                                    popUpTo(AppConstants.LOGIN_SCREEN_ROUTE){ inclusive = true}
                                                }
                                            }
                                        }


                                    }
                                    )
                            }
                            isProgress = !isProgress
                        }
                        .height(50.dp)
                        .background(Blue, shape = RoundedCornerShape(30.dp)),
                        contentAlignment = Alignment.Center,
                    ){

                        if (isProgress)
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.5.dp,
                            )
                        else
                            Text("Login",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontFamily = gilroy,
                                fontWeight = FontWeight.W500,
                                textAlign = TextAlign.Center,
                            )

                    }
                }


                Row (modifier = Modifier.fillMaxWidth()
                    .clickable {
                        navController.navigate(AppConstants.SIGNUP_SCREEN_ROUTE){
                            popUpTo(AppConstants.LOGIN_SCREEN_ROUTE) {
                                inclusive = true
                            }
                        }
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                ){

                    Text("Don't have an account ?",
                        fontSize = 13.sp,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.W300,
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = Modifier.width(5.dp))

                    Text("Sign Up",
                        fontSize = 14.sp,
                        color = Orange,
                        fontFamily = gilroy,
                        fontWeight = FontWeight.W500,
                        textAlign = TextAlign.Center,
                    )

                }

            }





        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginPagePreview() {
    val navController = rememberNavController()
    AndroidTaskTheme {
        LoginPage(navController)
    }
}