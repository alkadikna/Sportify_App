package com.example.sportify.Model

import android.annotation.SuppressLint
import android.icu.util.Calendar
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

    tempList.add(BadmintonField("Lapangan Badminton 1",true))
    tempList.add(BadmintonField("Lapangan Badminton 2",true))
    tempList.add(BadmintonField("Lapangan Badminton 3",true))

    tempList.add(TenisField("Lapangan Tenis 1",true))
    tempList.add(TenisField("Lapangan Tenis 2",true))
    tempList.add(TenisField("Lapangan Tenis 3",true))

    tempList.add(FutsalField("Lapangan Futsal 1",true))
    tempList.add(FutsalField("Lapangan Futsal 2",true))
    tempList.add(FutsalField("Lapangan Futsal 3",true))

    tempList.add(BasketField("Lapangan Basket 1",true))
    tempList.add(BasketField("Lapangan Basket 2",true))
    tempList.add(BasketField("Lapangan Basket 3",true))



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
    val time1314 = Time(
        "",
        13,
        14,
        fieldList = tempList
    )
    val time1415 = Time(
        "",
        14,
        15,
        fieldList = tempList
    )
    val time1516 = Time(
        "",
        15,
        16,
        fieldList = tempList
    )
    val time1617 = Time(
        "",
        16,
        17,
        fieldList = tempList
    )
    val time1718 = Time(
        "",
        17,
        18,
        fieldList = tempList
    )
    val time1819 = Time(
        "",
        18,
        19,
        fieldList = tempList
    )
    val time1920 = Time(
        "",
        19,
        20,
        fieldList = tempList
    )
    val time2021 = Time(
        "",
        20,
        21,
        fieldList = tempList
    )
    val time2122 = Time(
        "",
        21,
        22,
        fieldList = tempList
    )
    val time2223 = Time(
        "",
        22,
        23,
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
            time1314,
            time1415,
            time1516,
            time1617,
            time1718,
            time1819,
            time1920,
            time2021,
            time2122,
            time2223,
        )
        val schedule = Schedule(day, timeList.toMutableList())

        myRef.child(day).setValue(schedule)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }
}