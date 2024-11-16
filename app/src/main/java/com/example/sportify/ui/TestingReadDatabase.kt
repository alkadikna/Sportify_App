package com.example.sportify.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sportify.Model.BadmintonField
import com.example.sportify.Model.Field
import com.example.sportify.Model.TenisField
import com.example.sportify.Model.Time
import com.example.sportify.Repository.GetAllSchedule
import com.example.sportify.Repository.getScheduleByTime
import com.google.firebase.database.FirebaseDatabase

@Composable
fun TestingReadDB(){
    FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val timeList = remember { mutableStateListOf<Time>() }
    timeList.clear()
    LaunchedEffect(Unit) {
//        GetAllSchedule("15-11-2024", "tenis") { times ->
//            timeList.addAll(times)
//        }
        getScheduleByTime("futsal",8,11,"15-11-2024"){ times ->
            timeList.addAll(times)
        }
    }

    LazyColumn {
        items(timeList){ time ->
            ItemTime(time = time)
        }
    }
}

@Composable
fun ItemTime(time: Time, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Time: ${time.startTime}-${time.endTime}")
        if (time.fieldList.isNotEmpty()) {
            Text(text = "Fields:")
            time.fieldList.filter{it.name.contains("", ignoreCase = true)} .forEach { field ->
                Text(text = "- ${field.name}: ${if (field.isAvailable) "Available" else "Not Available"}")
            }
        } else {
            Text(text = "No fields available.")
        }
    }
}