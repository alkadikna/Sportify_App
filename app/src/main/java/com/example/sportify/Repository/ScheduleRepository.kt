package com.example.sportify.Repository

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalView
import com.example.sportify.Model.Time
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth



fun GetAllSchedule(selectedDate: String, fieldType: String, onComplete: (MutableList<Time>) -> Unit)  {
    var timeList = mutableListOf<Time>()
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val myRef = database.getReference("schedule").child("Day").child(selectedDate).child("timeList")
    myRef.get().addOnSuccessListener { dataSnapshot ->
        timeList.clear()
        if(dataSnapshot.exists()){
            for(Snapshot in dataSnapshot.children){
                val time = Snapshot.getValue(Time::class.java)
                if (time != null) {
                    filteredByField(time, fieldType)
                    timeList.add(time)
                }
            }
            onComplete(timeList)
        }
        else{
            onComplete(timeList)
        }
    }
}

fun filteredByField(time: Time, fieldType: String){
    val filtered = time.fieldList.filter { it.name.contains(fieldType, ignoreCase = true) }
    if(filtered.isNotEmpty()){
        time.fieldList = filtered.toMutableList()
    }
}

fun getScheduleByTime(fieldType: String, start: Int, end: Int, selectedDate: String, onComplete: (MutableList<Time>) -> Unit){
    var timeList = mutableListOf<Time>()
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val myRef = database.getReference("schedule").child("Day").child(selectedDate).child("timeList")
    myRef.get().addOnSuccessListener { dataSnapshot ->
        timeList.clear()
        if(dataSnapshot.exists()){
            for(Snapshot in dataSnapshot.children){
                val time = Snapshot.getValue(Time::class.java)
                if (time != null) {
                    if(time.startTime >= start && time.endTime <= end){
                        filteredByField(time, fieldType)
                        timeList.add(time)
                    }
                }
            }
            onComplete(timeList)
        }
        else{
            onComplete(timeList)
        }
    }
}

fun deleteOutdatedSchedule() {
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    val myRef = database.getReference("schedule").child("Day")
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))

    // Ambil tanggal hari ini tanpa waktu
    val currentDate = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.time

    myRef.get().addOnSuccessListener { dataSnapshot ->
        if (dataSnapshot.exists()) {
            for (snapshot in dataSnapshot.children) {
                val dateKey = snapshot.key // Ambil nama key sebagai tanggal
                if (dateKey != null) {
                    try {
                        val scheduleDate = dateFormat.parse(dateKey) // Parse tanggal dari key

                        if (scheduleDate != null) {
                            val scheduleCalendar = Calendar.getInstance().apply {
                                time = scheduleDate
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }

                            // Bandingkan tanggal saja tanpa waktu
                            if (scheduleCalendar.time.before(currentDate)) {
                                snapshot.ref.removeValue()
                                    .addOnSuccessListener {
                                        Log.d("Firebase", "Deleted outdated date: $dateKey")
                                    }.addOnFailureListener {
                                        Log.e("Firebase", "Failed to delete date: $dateKey", it)
                                    }
                            } else {
                                Log.d("Firebase", "Keeping current or future date: $dateKey")
                            }
                        }
                    } catch (e: ParseException) {
                        Log.e("Firebase", "Invalid date format for key: $dateKey", e)
                    }
                }
            }
        } else {
            Log.d("Firebase", "No schedule data found.")
        }
    }.addOnFailureListener {
        Log.e("Firebase", "Failed to fetch schedule data", it)
    }
}

fun deleteOutdatedTimes() {
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    val scheduleRef = database.getReference("schedule").child("Day")

    // Dapatkan tanggal hari ini
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
    val todayDate = dateFormat.format(Date()) // Format tanggal saat ini

    val calendar = Calendar.getInstance()
    val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
    val currentMinute = calendar.get(Calendar.MINUTE)
    val currentTotalMinutes = currentHour * 60 + currentMinute

    scheduleRef.get().addOnSuccessListener { scheduleSnapshot ->
        if (scheduleSnapshot.exists()) {
            for (daySnapshot in scheduleSnapshot.children) {
                val dateKey = daySnapshot.key // Ambil key tanggal

                // Hanya proses data jika tanggal cocok dengan hari ini
                if (dateKey == todayDate) {
                    val timeListRef = daySnapshot.child("timeList").ref

                    for (timeSnapshot in daySnapshot.child("timeList").children) {
                        val startTime = timeSnapshot.child("startTime").getValue(Int::class.java) ?: continue
                        val thresholdTime = startTime * 60 + 15 // Waktu threshold (startTime + 15 menit)

                        if (currentTotalMinutes > thresholdTime) {
                            // Hapus waktu yang sudah melebihi threshold
                            timeSnapshot.ref.removeValue()
                                .addOnSuccessListener {
                                    Log.d("Firebase", "Deleted outdated time: $startTime on $todayDate")
                                }
                                .addOnFailureListener {
                                    Log.e("Firebase", "Failed to delete time: $startTime on $todayDate", it)
                                }
                        }
                    }
                } else {
                    Log.d("Firebase", "Skipping date: $dateKey, not today.")
                }
            }
        } else {
            Log.d("Firebase", "No schedule data found.")
        }
    }.addOnFailureListener {
        Log.e("Firebase", "Failed to fetch schedule data", it)
    }
}




