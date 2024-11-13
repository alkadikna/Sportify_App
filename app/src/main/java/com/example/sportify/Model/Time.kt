package com.example.sportify.Model

import com.example.sportify.readField

class Time (

    var id: String? = null,
    var startTime: Float,
    var endTime: Float,
    var fieldList: List<Field>,

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
}