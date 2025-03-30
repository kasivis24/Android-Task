package com.mobile.android_task.ui.theme.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mobile.android_task.ui.theme.gilroy

@Composable
fun NotifyBanner(message : String){
    Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
        .border(border = BorderStroke(width = 1.dp, color = Color.White.copy(0.4f)), shape = RoundedCornerShape(15.dp))
        .background(color = Color.Gray.copy(alpha = 0.2f),shape = RoundedCornerShape(15.dp))){
        Text(
            modifier = Modifier.padding(5.dp),
            text = "$message",
            fontFamily = gilroy,
            fontSize = 13.sp,
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Center,
        )
    }
}

