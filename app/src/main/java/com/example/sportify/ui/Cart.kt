package com.example.sportify.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
import androidx.compose.material.Divider
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
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
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.White)
                    ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.logo),
//                            contentDescription = "logo",
//                            modifier = Modifier
//                                .align(Alignment.CenterHorizontally)
//                                .size(60.dp)
//                        )
                        Text(
                            text = "Checkout",
                            style = TextStyle(
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                            ),
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Divider(color = Color.Gray, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp)
                        ) {
                            items(cart) { item ->
                                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text(text = item.name, style = TextStyle(fontSize = 16.sp, color = Color.Gray))
                                    Text(text = "Tanggal: ${item.date}", style = TextStyle(fontSize = 16.sp, color = Color.Gray))
                                    Text(text = "Waktu: ${item.startTime}:00 - ${item.endTime}:00", style = TextStyle(fontSize = 16.sp, color = Color.Gray))
                                    Text(text = "Harga: ${item.price}", style = TextStyle(fontSize = 16.sp, color = Color.Gray))
                                    Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            TextButton(onClick = onDismissDialog) {
                                Text("Tutup", style = TextStyle(fontSize = 16.sp, color = Color.Red, fontWeight = FontWeight.Bold))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(
                                onClick = {
                                    val gson = Gson()
                                    val cartListJson = gson.toJson(cart)
                                    navCtrl.navigate("order/$cartListJson")
                                }
                            ) {
                                Text("Lanjut", style = TextStyle(fontSize = 16.sp, color = Color(0xFF008000), fontWeight = FontWeight.Bold))
                            }
                        }
                    }
                }
            )
        }
    }
}
