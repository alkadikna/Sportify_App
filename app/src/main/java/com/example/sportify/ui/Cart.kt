package com.example.sportify.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.sportify.Model.Field
import com.example.sportify.R
import com.google.gson.Gson

class Cart (
    var name: String = "",
    var date: String = "",
    var startTime: String = "",
    var endTime: String = "",
    var price: Double = 0.0
)

@Composable
fun ShowCartDialog(
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    cart: MutableList<Cart>,
    navCtrl: NavController
) {
    if (showDialog) {
        Dialog(onDismissRequest = onDismissDialog) {
            // Buat layout manual untuk dialog
            androidx.compose.material3.Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.padding(16.dp),
                content = {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Isi Keranjang Belanja",
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                            ),
                            modifier = Modifier
                                .padding(16.dp)
                                .align(Alignment.CenterHorizontally)

                        )
                        LazyColumn() {
                            items(cart) { item ->
                                Text(text = "Nama Lapangan: ${item.name}")
                                Text(text = "Tanggal: ${item.date}")
                                Text(text = "Waktu: ${item.startTime} - ${item.endTime}")
                                Text(text = "Harga: ${item.price}")
                            }
                        }
                        TextButton(onClick = onDismissDialog) {
                            Text("Tutup")
                        }

                        TextButton(
                            onClick = {
                                val gson = Gson()
                                val cartListJson = gson.toJson(cart)
                                navCtrl.navigate("order/$cartListJson")
                            }
                        ) {
                            Text("Lanjut")
                        }
                    }
                }
            )
        }
    }
}