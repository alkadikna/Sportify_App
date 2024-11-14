package com.example.sportify.Model

import com.example.sportify.readField
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: FirebaseDatabase


class Time (

    var id: String? = null,
    var startTime: Int,
    var endTime: Int,
    var fieldList: MutableList<Field>,

){
    fun SpecialPrice(){
        if (startTime >= 16){
            fieldList.forEach { field ->
                field.extraPrice()
            }
        }
    }

    fun getAllField(){
        readField()
    }
    fun getField(id: String){

    }
    fun getAllField(onComplete: (List<Field>) -> Unit)  {
        val myRef = database.getReference("schedule").child("time")
        myRef.get().addOnSuccessListener { dataSnapshot ->
            fieldList.clear()
            if(dataSnapshot.exists()){
                for(Snapshot in dataSnapshot.children){
                    val field = dataSnapshot.getValue(Field::class.java)!!
                    fieldList.add(field)
                }
                onComplete(fieldList)
            }
            else{
                onComplete(emptyList())
            }
        }
    }
}