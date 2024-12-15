package com.example.sportify.ui

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.Model.Receipt
import com.example.sportify.Model.ReceiptItem
import com.example.sportify.R
import com.example.sportify.Repository.getReceiptData
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


@Composable
fun ReceiptLayout(navCtrl: NavController, auth: FirebaseAuth, receiptKey: String) {
    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val currentUser = auth.currentUser
    //var username by remember { mutableStateOf("-") }
    var receipt by remember { mutableStateOf<Receipt?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { userId ->
            getReceiptData(
                userId = userId,
                receiptKey = receiptKey,
                onSuccess = { fetchedReceipt ->
                    receipt = fetchedReceipt
                },
                onError = { error ->
                    errorMessage = error
                }
            )
        }
    }

    Scaffold(
        topBar = {
            Box {
                TopSection()

                IconButton(
                    onClick = { navCtrl.navigate("home") },
                    modifier = Modifier.padding(start = 5.dp, top = 5.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.back_arrow),
                        contentDescription = "back",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        bottomBar = {}
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (errorMessage != null) {
                Text(
                    text = "Error: $errorMessage",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (receipt == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(Alignment.Center)
                ) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    PaymentDetailCard(receipt)
                    Spacer(modifier = Modifier.height(16.dp))
                    OrderDetailCard(receipt!!.items)
                }
            }
        }
    }
}

@Composable
fun PaymentDetailCard(receipt: Receipt?) {
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
            PaymentDetailRow(label = "ID Pesanan", value = receipt?.orderId ?: "-")
            PaymentDetailRow(label = "Dipesan Oleh", value = receipt?.username ?: "-")
            PaymentDetailRow(label = "Waktu Pemesanan", value = receipt?.orderTime ?: "-")
            PaymentDetailRow(label = "Jumlah Pembayaran", value = "Rp. ${receipt?.totalAmount ?: 0.0}")
        }
    }
}

@Composable
fun OrderDetailCard(items: List<ReceiptItem>?) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Detail Pesanan",
                style = TextStyle(
                    fontSize = 22.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                )
            )
            Spacer(modifier = Modifier.height(13.dp))

            if (!items.isNullOrEmpty()) {
                LazyColumn {
                    items(items) { item ->
                        OrderDetailRow(item.name, item.schedule, item.price)
                    }
                }

                Divider(color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(13.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Jumlah Pembayaran",
                        style = TextStyle(
                            fontSize = 17.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        )
                    )
                    Text(
                        text = "Rp. ${items.sumOf { it.price }}",
                        style = TextStyle(
                            fontSize = 17.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        )
                    )
                }
            } else {
                Text(
                    text = "Tidak ada item dalam struk.",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.inria_serif_regular)),
                    )
                )
            }
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

//@Preview
//@Composable
//private fun OrderDetailCardPreview() {
//    val sampleCartList = listOf(
//        Cart("A3", "Sabtu, 19 Okt 2024", "14", "16", 8000.0),
//        Cart("B1", "Sabtu, 19 Okt 2024", "14", "16", 8000.0)
//    )
//
//    OrderDetailCard(sampleCartList)
//}
