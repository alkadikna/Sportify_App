package com.example.sportify.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
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
fun PaymentLayout(navCtrl: NavController, cartListJson: String) {
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val gson = Gson()
    val listType = object : TypeToken<List<Cart>>() {}.type
    val cartList: List<Cart> = gson.fromJson(cartListJson, listType)

    Scaffold(
        topBar = { TopSection() },
        bottomBar = { /* Bottom bar kosong */ }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Total Pembayaran
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Pembayaran",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            color = Color.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Rp. ${cartList.sumOf { it.price }}",
                        style = TextStyle(
                            fontSize = 28.sp,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                            color = Color.Black
                        )
                    )
                }
            }

            // Opsi Pembayaran
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    PaymentOptionItem(
                        R.drawable.gopay_logo,
                        label = "gopay",
                        paymentMethod = {}
                    )
                }
                item {
                    PaymentOptionItem(
                        R.drawable.ovo_logo,
                        label = "OVO",
                        paymentMethod = {}
                    )
                }
                item {
                    PaymentOptionItem(
                        R.drawable.sopi_logo,
                        label = "ShopeePay",
                        paymentMethod = {}
                    )
                }
                item {
                    PaymentOptionItem(
                        R.drawable.dana_logo,
                        label = "DANA",
                        paymentMethod = {}
                    )
                }
                item {
                    PaymentOptionItem(
                        R.drawable.qris,
                        label = "QRIS",
                        paymentMethod = {}
                    )
                }
                item {
                    PaymentOptionItem(
                        R.drawable.cash_payment,
                        label = "Cash Payment",
                        paymentMethod = {
                            updateField(cartList)
                            val gson = Gson()
                            val cartListJson = gson.toJson(cartList)
                            navCtrl.navigate("order/$cartListJson/receipt")
                            cartList.forEach { saveOrder(it) }
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentOptionItem(
    iconRes: Int,
    label: String,
    paymentMethod: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(2f),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        onClick = paymentMethod
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(110.dp),
                tint = Color.Unspecified
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPaymentScreen() {
    val navController = rememberNavController()
    PaymentLayout(navController, "sampleCartList")
}

//@Preview(showBackground = true)
//@Composable
//fun PreviewPaymentOptionItem() {
//    val navController = rememberNavController()
//    PaymentOptionItem(
//        iconRes = R.drawable.flat_color_icons_google,
//        label = "gopay",
//        paymentMethod = {}
//    )
//}




