package com.example.sportify.Model

open class Field(

    var id: String? = null,
    var name: String = "",
    var isAvailable: Boolean = true,
    var price: Int = 0,
    var user: String = ""

){
    fun extraPrice(){
        // example
        this.price += 10000
    }
}
