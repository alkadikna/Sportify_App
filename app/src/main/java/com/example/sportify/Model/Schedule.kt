package com.example.sportify.Model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth

class Schedule (
    var timeList: MutableList<Time>

){
    fun getAllSchedule(onComplete: (List<Time>) -> Unit)  {
        val myRef = database.getReference("schedule")
        myRef.get().addOnSuccessListener { dataSnapshot ->
            timeList.clear()
            if(dataSnapshot.exists()){
                for(Snapshot in dataSnapshot.children){
                    val time = dataSnapshot.getValue(Time::class.java)!!
                    timeList.add(time)
                }
                onComplete(timeList)
            }
            else{
                onComplete(emptyList())
            }
        }
    }

    fun getScheduleByRangeTime(onComplete: (List<Time>) -> Unit, start: Int, end: Int){
        val myRef = database.getReference("schedule")
        myRef.orderByChild("startTime").startAt(start.toDouble()).get().addOnSuccessListener { dataSnapshot ->
            timeList.clear()
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val time = snapshot.getValue(Time::class.java)

                    // Melakukan filtering di sisi klien untuk endTime <= 4
                    val endValue = snapshot.child("endTime").getValue(Int::class.java)
                    if (endValue != null && endValue <= end) {
                        time?.let { timeList.add(it) }
                    }
                }
                onComplete(timeList)
            } else {
                onComplete(emptyList())
            }
        }
    }

    fun getScheduleById(onComplete: (List<Time>) -> Unit, id: String?){
        val myRef = database.getReference("schedule")
        myRef.orderByChild("id").equalTo(id).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                for (snapshot in dataSnapshot.children) {
                    val time = snapshot.getValue(Time::class.java)
                    time?.let { timeList.add(it) }
                }
                onComplete(timeList)
            } else {
                onComplete(emptyList())
            }
        }
    }
}