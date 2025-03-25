package com.mobile.android_task.viewmodel

import android.text.BoringLayout
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun signUpWithEmail(email: String, password: String,state : (String) -> Unit,eventState : (Boolean) -> Unit) {

        val isVaildEmail : String = validateEmail(email)
        val isVaildPassword : String = validatePassword(password)

        if (isVaildEmail.equals("") && isVaildPassword.equals("")){
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    viewModelScope.launch {
                        val stateData : String = if (task.isSuccessful) "Success" else "${task.exception?.message}"
                        state(stateData)
                        if (task.isSuccessful){
                            eventState(true)
                        }else {
                            eventState(false)
                        }
                    }

                }
        }else {
            if (!isVaildEmail.equals("") && !isVaildPassword.equals("")){
                state("${isVaildEmail +" "+ isVaildPassword}")
            }
            else if (!isVaildEmail.equals("")){
                state(isVaildEmail)
            }
            else {
                state(isVaildPassword)
            }
        }

    }

    fun signInWithEmail(email: String, password: String,state : (String) -> Unit,eventState : (Boolean) -> Unit) {

        val isVaildEmail : String = validateEmail(email)
        val isVaildPassword : String = validatePassword(password)

        if (isVaildEmail.equals("") && isVaildPassword.equals("")){
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    viewModelScope.launch {
                        val stateData : String = if (task.isSuccessful) "Success" else "${task.exception?.message}"
                        state(stateData)
                        if (task.isSuccessful){
                            eventState(true)
                        }else {
                            eventState(false)
                        }
                    }

                }
        }else {
            if (!isVaildEmail.equals("") && !isVaildPassword.equals("")){
                state("${isVaildEmail +" "+ isVaildPassword}")
            }
            else if (!isVaildEmail.equals("")){
                state(isVaildEmail)
            }
            else {
                state(isVaildPassword)
            }
        }

    }



    fun validateEmail(email: String): String {
        val regex = Regex("^[A-Za-z0-9._%+-]+@gmail\\.com\$")
        return if (email.isEmpty()) {
            "Email is required"
        } else if (!regex.matches(email)) {
            "Enter a valid Gmail address"
        } else {
            ""
        }
    }

    fun validatePassword(password: String): String {
        return when {
            password.isEmpty() -> "Password is required"
            password.length < 6 -> "Password must be at least 6 characters"
            else -> ""
        }
    }

}
