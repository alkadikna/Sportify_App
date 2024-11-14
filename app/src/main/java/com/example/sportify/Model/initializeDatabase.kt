package com.example.sportify.Model

import com.example.sportify.create
import com.example.sportify.createTime
import com.example.sportify.readField
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: FirebaseDatabase


fun InitDb(){
    val tempList: MutableList<Field> = mutableListOf()

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
    val myRef = database.getReference("schedule")
    create(time89, myRef)
    create(time910, myRef)
    create(time1011, myRef)
    create(time1112, myRef)
    create(time1213, myRef)

//    createTime(time89, "schedule")
//    createTime(time910, "schedule")
//    createTime(time1011, "schedule")
//    createTime(time1112, "schedule")
//    createTime(time1213, "schedule")

}