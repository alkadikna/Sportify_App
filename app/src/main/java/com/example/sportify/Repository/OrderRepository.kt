package com.example.sportify.Repository

import com.example.sportify.Model.Time
import com.example.sportify.ui.Cart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth

fun updateField(cartList: List<Cart>) {
    // Inisialisasi database
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    var auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Ambil username dari database
    val refUser = database.getReference("users/${user?.uid!!}/username")
    refUser.get().addOnSuccessListener { unss ->
        val username = unss.value.toString()

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
                                    val fieldRef = myRef.child(snapshot.key!!)
                                        .child("fieldList")
                                        .child(index.toString())

                                    fieldRef.setValue(field)
                                    fieldRef.child("user").setValue(username)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

fun saveOrder(cartList: List<Cart>){
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    var auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val myRef = database.getReference("users").child(user?.uid!!).child("Order")
    myRef.setValue(cartList)
}

fun getOrder(onComplete: (MutableList<Cart>) -> Unit){
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    var auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    val myRef = database.getReference("users").child(user?.uid!!).child("Order")
    var orderList = mutableListOf<Cart>()

    myRef.get().addOnSuccessListener { dataSnapshot ->
        orderList.clear()
        if(dataSnapshot.exists()){
            for(Snapshot in dataSnapshot.children){
                val order = Snapshot.getValue(Cart::class.java)
                if (order != null) {
                    orderList.add(order)
                }
            }
            onComplete(orderList)
        }
        else{
            onComplete(orderList)
        }
    }
}