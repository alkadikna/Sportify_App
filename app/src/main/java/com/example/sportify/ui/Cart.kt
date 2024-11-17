package com.example.sportify.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.AlertDialog
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.sportify.Model.Field

class Cart (
    var name: String,
    var date: String,
    var startTime: String,
    var endTime: String,
    var price: Float
)

@Composable
fun ShowCartDialog(
    showDialog: Boolean,
    onDismissDialog: () -> Unit,
    cart: MutableList<Cart>
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
                        Text(text = "Isi Keranjang Belanja")
                        LazyColumn {
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
                    }
                }
            )

        }
    }
}