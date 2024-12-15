package com.example.sportify.Repository

import android.util.Log
import com.example.sportify.Model.Receipt
import com.example.sportify.ui.Cart
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private lateinit var database: FirebaseDatabase
private  lateinit var auth: FirebaseAuth

fun generateOrderID(): String {
    val prefix = "SPTF"
    val randomNumber = (100000..999999).random()
    return "$prefix$randomNumber"
}


fun getCurrentDateTime(): String {
    val currentDateTime = Date()
    val formatter = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    return formatter.format(currentDateTime)
}


fun saveReceipt(
    cartList: List<Cart>,
    onSuccess: (String) -> Unit,
    onError: (String) -> Unit
) {
    auth = Firebase.auth
    val uid = auth.currentUser?.uid ?: run {
        onError("Pengguna tidak ditemukan.")
        return
    }

    val orderId = generateOrderID()
    val orderTime = getCurrentDateTime()
    val totalAmount = cartList.sumOf { it.price }

    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    val usernameRef = database.getReference("users").child(uid)

    usernameRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val username = snapshot.child("username").getValue(String::class.java) ?: "-"

            val receiptData = mapOf(
                "orderId" to orderId,
                "orderTime" to orderTime,
                "username" to username,
                "items" to cartList.map { item ->
                    mapOf(
                        "name" to item.name,
                        "schedule" to item.date,
                        "price" to item.price
                    )
                },
                "totalAmount" to totalAmount
            )

            val receiptRef = database.getReference("users").child(uid).child("receipts")
            val receiptKey = receiptRef.push().key // Generate a unique key for the receipt

            if (receiptKey != null) {
                receiptRef.child(receiptKey).setValue(receiptData)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("Receipt", "Struk berhasil disimpan untuk pengguna: $uid")
                            onSuccess(receiptKey)
                        } else {
                            Log.e("Receipt", "Gagal menyimpan struk: ${task.exception?.message}")
                            onError("Gagal menyimpan struk: ${task.exception?.message}")
                        }
                    }
            } else {
                onError("Gagal membuat kunci struk.")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            Log.e("Receipt", "Gagal mengambil data pengguna: ${error.message}")
            onError("Gagal mengambil data pengguna: ${error.message}")
        }
    })
}


fun fetchUserListReceipt(
    userId: String,
    onSuccess: (List<Pair<String, Receipt>>) -> Unit, // List of receiptKey and Receipt
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    val receiptRef = database.getReference("users").child(userId).child("receipts")

    receiptRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val receipts = mutableListOf<Pair<String, Receipt>>()
            for (receiptSnapshot in snapshot.children) {
                val receipt = receiptSnapshot.getValue(Receipt::class.java)
                val receiptKey = receiptSnapshot.key
                if (receipt != null && receiptKey != null) {
                    receipts.add(Pair(receiptKey, receipt))
                }
            }
            onSuccess(receipts)
        }

        override fun onCancelled(error: DatabaseError) {
            onError(error.message)
        }
    })
}

fun getReceiptData(
    userId: String,
    receiptKey: String,
    onSuccess: (Receipt) -> Unit,
    onError: (String) -> Unit
) {
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    val receiptRef = database.getReference("users").child(userId).child("receipts").child(receiptKey)

    receiptRef.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val receipt = snapshot.getValue(Receipt::class.java)
            if (receipt != null) {
                onSuccess(receipt)
            } else {
                onError("Data receipt tidak ditemukan.")
            }
        }

        override fun onCancelled(error: DatabaseError) {
            onError("Gagal memuat data: ${error.message}")
        }
    })
}


