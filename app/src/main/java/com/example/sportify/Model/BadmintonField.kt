package com.example.sportify.Model

class BadmintonField(

    name: String,
    isAvailable: Boolean,
    price: Int = 40000

) : Field("", name, isAvailable, price)  {

}