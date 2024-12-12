package com.example.sportify.layout_component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun BottomSheet(onSelectSport: (String) -> Unit) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Cabang Olahraga", fontSize = 20.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        val sports = listOf("Badminton", "Basket", "Tenis", "Futsal")
        sports.forEach { sport ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectSport(sport) }
                    .padding(vertical = 8.dp)
            ) {
                Text(text = sport, fontSize = 16.sp, modifier = Modifier.weight(1f))
            }
        }
    }
}

