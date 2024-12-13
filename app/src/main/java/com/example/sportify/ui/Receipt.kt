package com.example.sportify.ui

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.R
import com.example.sportify.layout_component.TopSection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private lateinit var database: FirebaseDatabase

@Composable
fun generateOrderID(): String {
    val prefix = "SPTF"
    val randomNumber = (100000..999999).random()
    return "$prefix$randomNumber"
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun getCurrentDateTime(): String {
    val currentDateTime = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
    return currentDateTime.format(formatter)
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReceiptLayout(cartListJson: String, auth: FirebaseAuth) {
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val gson = Gson()
    val listType = object : TypeToken<List<Cart>>() {}.type
    val cartList: List<Cart> = gson.fromJson(cartListJson, listType)

    val currentUser = auth.currentUser
    var username by remember { mutableStateOf("-") }

    fun fetchUserData(uid: String) {
        val userData = database.getReference("users").child(uid)
        userData.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                username = snapshot.child("username").getValue(String::class.java) ?: "-"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ReceiptLayout", "Failed to fetch user data: ${error.message}")
            }
        })
    }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { fetchUserData(it) }
    }

    Scaffold(
        topBar = { TopSection() },
        bottomBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                PaymentDetailCard(cartList, username)
                Spacer(modifier = Modifier.height(16.dp))
                OrderDetailCard(cartList)
            }
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun PaymentDetailCard(cartList: List<Cart>, username: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Detail Pembayaran",
                style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                )
            )
            Spacer(modifier = Modifier.height(13.dp))
            PaymentDetailRow(label = "ID Pesanan", value = generateOrderID())
            PaymentDetailRow(label = "Dipesan Oleh", value = username)
            PaymentDetailRow(label = "Waktu Pemesanan", value = getCurrentDateTime())
            PaymentDetailRow(label = "Jumlah Pembayaran", value = "Rp. ${cartList.sumOf { it.price }}")
        }
    }
}

@Composable
fun PaymentDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_regular)),
            )
        )
        Text(
            text = value,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_regular)),
            )
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
fun OrderDetailCard(cartList: List<Cart>) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Detail Pesanan",
                style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                )
                )
            Spacer(modifier = Modifier.height(13.dp))

            LazyColumn {
                items(cartList) { item ->
                    OrderDetailRow(item.name, item.date, item.price)
                }
            }

            Divider(color = Color.Gray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(13.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Jumlah Pembayaran",
                    style = TextStyle(
                        fontSize = 17.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                    )
                )
                Text(text = "Rp. ${cartList.sumOf { it.price }}",
                    style = TextStyle(
                        fontSize = 17.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                    )
                )
            }
        }
    }
}

@Composable
fun OrderDetailRow(name: String, schedule: String, price: Double) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = name,
            style = TextStyle(
                fontSize = 15.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_regular)),
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = schedule,
                style = TextStyle(
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                ),
                modifier = Modifier.alpha(0.7f)
            )

            Text(
                text = "Rp. $price",
                style = TextStyle(
                    fontSize = 13.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_regular)),
                ),
                modifier = Modifier.alpha(0.7f)
            )


        }

        Spacer(modifier = Modifier.height(3.dp))
    }
    Spacer(modifier = Modifier.height(20.dp))
}

//@Preview(showBackground = true)
//@Composable
//private fun ReceiptLayoutPreview() {
//    ReceiptLayout("")
//}

@Preview
@Composable
private fun OrderDetailCardPreview() {
    val sampleCartList = listOf(
        Cart("A3", "Sabtu, 19 Okt 2024", "14", "16", 8000.0),
        Cart("B1", "Sabtu, 19 Okt 2024", "14", "16", 8000.0)
    )

    OrderDetailCard(sampleCartList)
}