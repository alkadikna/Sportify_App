package com.example.sportify.Repository

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.sportify.Model.Time
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth


fun GetAllSchedule(onComplete: (MutableList<Time>) -> Unit)  {
    var timeList = mutableListOf<Time>()
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val myRef = database.getReference("schedule").child("Day").child("15-11-2024").child("timeList")
    myRef.get().addOnSuccessListener { dataSnapshot ->
        timeList.clear()
        if(dataSnapshot.exists()){
            for(Snapshot in dataSnapshot.children){
                val time = Snapshot.getValue(Time::class.java)
                if (time != null) {
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