package com.example.sportify.Model

class Order(

    var time: Time,
    var field: Field
){
    fun makeOrder(){
        field.isAvailable = false
    }

    fun printNota(){

    }
}