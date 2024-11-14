package com.example.sportify.Model

import android.annotation.SuppressLint
import android.icu.util.Calendar
import androidx.compose.animation.shrinkVertically
import com.example.sportify.create
import com.example.sportify.createTime
import com.example.sportify.readField
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private lateinit var database: FirebaseDatabase


@SuppressLint("SimpleDateFormat")
fun InitDb(){
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val tempList: MutableList<Field> = mutableListOf()
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val dates = mutableListOf<Date>()

    tempList.add(BadmintonField("lapangan Badminton 1",true))
    tempList.add(BadmintonField("lapangan Badminton 2",true))
    tempList.add(BadmintonField("lapangan Badminton 3",true))

    tempList.add(TenisField("lapangan Tenis 1",true))
    tempList.add(TenisField("lapangan Tenis 2",true))
    tempList.add(TenisField("lapangan Tenis 3",true))

    tempList.add(FutsalField("lapangan Futsal 1",true))
    tempList.add(FutsalField("lapangan Futsal 2",true))
    tempList.add(FutsalField("lapangan Futsal 3",true))


    val time89 = Time(
        "",
        8,
        9,
        fieldList = tempList
        )
    val time910 = Time(
        "",
        9,
        10,
        fieldList = tempList
    )
    val time1011 = Time(
        "",
        10,
        11,
        fieldList = tempList
    )
    val time1112 = Time(
        "",
        11,
        12,
        fieldList = tempList
    )
    val time1213 = Time(
        "",
        12,
        13,
        fieldList = tempList
    )
    val myRef = database.getReference("schedule").child("Day")
//    create(time89, myRef)
//    create(time910, myRef)
//    create(time1011, myRef)
//    create(time1112, myRef)
//    create(time1213, myRef)

    for(i in 1 until 7){
        val day = dateFormat.format(calendar.time)
        val timeList = listOf<Time>(
            time89,
            time910,
            time1112,
            time1011,
            time1213,
        )
        val schedule = Schedule(day, timeList.toMutableList())

        myRef.child(day).setValue(schedule)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
}