package com.example.sportify.Model

import com.example.sportify.readTime

class Schedule (
    var timeList: List<Time>

){
    fun getAllSchedule(){
        readTime()
    }
}