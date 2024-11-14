package com.example.sportify.layout_component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sportify.R

@Composable
public fun TopSectionWithImage() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Color(0xFF5AB5FF))
            .fillMaxWidth()
            .height(190.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.image_6), // Replace with your icon resource
            contentDescription = "App Icon",
            modifier = Modifier.fillMaxSize()
        )
    }
}