package com.example.sportify.Repository

import com.example.sportify.Model.Time
import com.example.sportify.ui.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth

//fun updateAvailability(cartList: List<Cart>){
//
//    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
//
//    for (cart in cartList){
//        val myRef = database.getReference("schedule").child("Day").child(cart.date).child("timeList")
//        var timeList = mutableListOf<Time>()
//        myRef.get().addOnSuccessListener { dataSnapshot ->
//            if(dataSnapshot.exists()){
//                for(Snapshot in dataSnapshot.children){
//                    val time = Snapshot.getValue(Time::class.java)
//                    if (time != null) {
//                        if(time.startTime == cart.startTime.toInt() && time.endTime == cart.endTime.toInt()){
//                            timeList.add(time)
//                        }
//                    }
//                }
//            }
//        }
//        for(timeItem in timeList){
//            for(field in timeItem.fieldList){
//                if(field.name == cart.name){
//                    field.isAvailable = false
//                    field.id?.let { myRef.child("fieldList").child(it) }?.setValue(field)
//                }
//            }
//        }
//    }
//}

fun updateAvailability(cartList: List<Cart>) {
    // Inisialisasi database
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    // Iterasi melalui daftar cart
    for (cart in cartList) {
        // Referensi ke path schedule
        val myRef = database.getReference("schedule/Day/${cart.date}/timeList")

        myRef.get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                // Iterasi melalui timeList dari Firebase
                for (snapshot in dataSnapshot.children) {
                    val time = snapshot.getValue(Time::class.java)

                    if (time != null && time.startTime == cart.startTime.toInt() && time.endTime == cart.endTime.toInt()) {
                        // Cari field yang cocok
                        time.fieldList.forEachIndexed { index, field ->
                            if (field.name == cart.name) {
                                // Update availability
                                field.isAvailable = false
                                // Update data di Firebase
                                myRef.child(snapshot.key!!) // Key dari timeList
                                    .child("fieldList")
                                    .child(index.toString()) // Index dari fieldList
                                    .setValue(field)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun saveOrder(cartList: List<Cart>){
    val myRef = database.getReference("users").child(auth.uid!!).child("Order")
    myRef.setValue(cartList)
}
