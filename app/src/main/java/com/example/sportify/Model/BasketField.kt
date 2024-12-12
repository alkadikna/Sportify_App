package com.example.sportify.Model

class BasketField (
    name: String,
    isAvailable: Boolean,
    price: Int = 50000

) : Field("", name, isAvailable, price)
{

}