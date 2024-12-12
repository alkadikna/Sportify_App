package com.example.sportify.layout_component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.sportify.R

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FloatingCartButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.padding(16.dp)
    ) {
        FloatingActionButton(
            onClick = onClick,
            modifier = Modifier.align(Alignment.BottomEnd),
            containerColor = colorResource(id = R.color.main_blue)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Keranjang",
                tint = Color.White
            )
        }
    }
}