package com.example.sportify.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.sportify.Model.Field
import com.example.sportify.Model.Time
import com.example.sportify.R
import com.example.sportify.Repository.getScheduleByTime
import com.google.firebase.database.FirebaseDatabase


@Composable
fun TestingReadDB(fieldType: String, start: Int, end: Int, selectedDate: String = ""){
    FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val timeList = remember { mutableStateListOf<Time>() }
    timeList.clear()
    LaunchedEffect(Unit) {
        getScheduleByTime(fieldType, start, end, selectedDate){ times ->
            timeList.addAll(times)
        }
    }

    LazyColumn {
        items(timeList){ time ->
            // List lapangan untuk waktu tertentu
            time.fieldList.filter { it.name.contains("", ignoreCase = true) }
                .forEach { field ->
                    if (field.isAvailable) {
                        FieldItem(field = field, selectedDate = selectedDate, startTime = time.startTime, endTime = time.endTime)
                    }
                }
        }

    }
}

@SuppressLint("DefaultLocale")
fun formatHour(hour: Int): String {
    return String.format("%02d.00", hour)
}

@Composable
fun FieldItem(field: Field, selectedDate: String, startTime: Int, endTime: Int) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Lapangan
            Image(
                painter = painterResource(id = R.drawable.sample_field), // Replace with your image resource
                contentDescription = "Field Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Informasi Lapangan
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = field.name)
                Text(text = selectedDate)
                Text(
                    text = "${formatHour(startTime)} - ${formatHour(endTime)}",
                )
            }
            // Ikon Panah
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow Icon",
                modifier = Modifier.size(24.dp)
            )
        }
    }
}