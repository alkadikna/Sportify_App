package com.example.sportify.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.R
import com.example.sportify.Repository.saveOrder
import com.example.sportify.Repository.updateField
import com.example.sportify.layout_component.TopSection
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

private lateinit var database: FirebaseDatabase

@Composable
fun OrderLayout(navCtrl: NavController, cartListJson: String, modifier: Modifier = Modifier) {

    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val gson = Gson()
    val listType = object : TypeToken<List<Cart>>() {}.type
    val cartList = remember {
        mutableStateListOf<Cart>().apply {
            addAll(gson.fromJson(cartListJson, listType))
        }
    }

    Scaffold(
        topBar = { TopSection() },
        bottomBar = { BottomPaymentSection(navCtrl, cartList.sumOf { it.price }, cartList)},
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .weight(0.7f, fill = false),
                elevation = 4.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Jadwal Pemesanan",
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Box(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .padding(bottom = 8.dp)
                    ) {
                        BookingList(
                            cartList = cartList,
                            onRemove = { item -> cartList.remove(item) }
                        )
                    }

//                    Text(
//                        text = "+ Tambah Pesan",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                //
//                            }
//                            .padding(vertical = 16.dp),
//                        textAlign = TextAlign.Center,
//                        color = Color.Blue,
//                        fontWeight = FontWeight.Bold
//                    )
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = 4.dp
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.receipt_sharp),
                        contentDescription = "",
                        modifier = Modifier.size(35.dp),
                        tint = Color.Black
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Tambahkan Promo",
                        style = TextStyle(
                            fontSize = 18.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        ),
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.weui_arrow_filled),
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Ringkasan Pembayaran",
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        ),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(text = "Harga Sewa",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            modifier = Modifier
                                .graphicsLayer { this.alpha = 0.7f }
                            )
                        Text(text = "Rp ${cartList.sumOf { it.price }}",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            modifier = Modifier
                                .graphicsLayer { this.alpha = 0.7f }
                            )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(text = "Harga Tambahan",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            modifier = Modifier
                                .graphicsLayer { this.alpha = 0.7f }
                            )
                        Text(text = "Rp 0",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            modifier = Modifier
                                .graphicsLayer { this.alpha = 0.7f })
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Divider()
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                    ) {
                        Text(text = "Jumlah Pembayaran",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                            ),
                            )
                        Text(text = "Rp ${cartList.sumOf { it.price }}",
                            style = TextStyle(
                                fontSize = 14.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingList(cartList: List<Cart>, onRemove: (Cart) -> Unit) {
    LazyColumn {
        items(cartList) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = 0.dp
            ) {
                Column {
                    Text(
                        text = item.name,
                        style = TextStyle(
                            fontSize = 22.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        ),
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = item.date,
                        style = TextStyle(
                            fontSize = 15.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        ),
                        modifier = Modifier
                            .graphicsLayer { this.alpha = 0.7f }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "${item.startTime}:00 - ${item.endTime}:00",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            modifier = Modifier
                                .graphicsLayer { this.alpha = 0.7f }
                        )

                        Text(
                            text = "Rp. ${item.price}",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            ),
                            modifier = Modifier
                                .graphicsLayer { this.alpha = 0.7f }
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { onRemove(item) },
                            colors = ButtonDefaults.buttonColors(Color(250, 64, 50)),
                            shape = RoundedCornerShape(13.dp),
                            elevation = ButtonDefaults.buttonElevation(7.dp),
                            modifier = Modifier
                                .padding(vertical = 7.dp, horizontal = 7.dp)
                                .padding(start = 7.dp, end = 7.dp)
                                //.weight(1f)
                        ) {
                            Text(
                                text = "Remove",
                                style = TextStyle(
                                    fontSize = 15.sp,
                                    color = Color.White,
                                    fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomPaymentSection(navCtrl: NavController, totalPrice: Double, cartList: List<Cart>) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(7.dp, shape = RectangleShape, clip = false),
        color = Color.White,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Jumlah Pembayaran",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                    ),
                )
                Text(text = "Rp.${totalPrice}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                    ),
                    )
            }

            Button(
                onClick = {
                    //updateField(cartList)
                    val gson = Gson()
                    val cartListJson = gson.toJson(cartList)
                    navCtrl.navigate("order/$cartListJson/payment")
                    //cartList.forEach { saveOrder(it) }
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier
                    .weight(1f),
                colors = ButtonDefaults.buttonColors(Color(90, 181, 255))
            ) {
                Text(
                    text = "Pesan",
                    style = TextStyle(
                        fontSize = 15.sp,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                    ),
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun PreviewBookingList() {
    val sampleCartList = listOf(
        Cart("A3", "Sabtu, 19 Okt 2024", "14", "16", 8000.0),
        Cart("B1", "Sabtu, 19 Okt 2024", "14", "16", 8000.0)
    )
    BookingList(
        cartList = sampleCartList,
        onRemove = {}
    )
}

//@Preview(showBackground = true)
//@Composable
//private fun PreviewBottomPaymentSection() {
//    BottomPaymentSection(rememberNavController(),  1500.0)
//}


