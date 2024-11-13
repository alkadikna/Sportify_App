package com.example.sportify.Model

open class Field(

    var id: String? = null,
    var isAvailable: Boolean,
    var price: Int

){
    fun extraPrice(){
        // example
        this.price += 10000
    }
}
