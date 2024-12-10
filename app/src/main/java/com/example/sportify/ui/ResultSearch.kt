package com.example.sportify.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sportify.Model.Field
import com.example.sportify.Model.Time
import com.example.sportify.R
import com.example.sportify.Repository.FetchScheduleData
import com.example.sportify.Repository.getScheduleByTime
import com.example.sportify.layout_component.FloatingCartButton
import com.google.firebase.database.FirebaseDatabase


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TestingReadDB(navCtrl: NavController, fieldType: String, start: Int, end: Int, selectedDate: String = ""){
    FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val timeList = remember { mutableStateListOf<Time>() }
    val cartList = remember { mutableStateListOf<Cart>() }
    var showDialog by remember { mutableStateOf(false) }

//    timeList.clear()
    LaunchedEffect(Unit) {
        if(start == 0 && end == 0){
            FetchScheduleData(selectedDate, fieldType){ times ->
                timeList.addAll(times)
            }
        }
        else{
            getScheduleByTime(fieldType, start, end, selectedDate){ times ->
                timeList.addAll(times)
            }
        }
    }

    LazyColumn {
        items(timeList){ time ->
            // List lapangan untuk waktu tertentu
            time.fieldList.filter { it.name.contains("", ignoreCase = true) }
                .forEach { field ->
                    if (field.isAvailable) {
//                        FieldItem(field = field, selectedDate = selectedDate, startTime = time.startTime, endTime = time.endTime
//                        , addCart = { cart ->
//                                cartList.add(cart)
//                            }
//                        )
                        FieldItem(field = field, selectedDate = selectedDate, startTime = time.startTime, endTime = time.endTime,
                            addCart = { item ->
                                cartList.add(item)
                            }
                        )
                    }
                }
        }
    }
    FloatingCartButton(onClick = {showDialog = true})
    ShowCartDialog(showDialog = showDialog, onDismissDialog = { showDialog = false }, cart = cartList, navCtrl = navCtrl)
}

@SuppressLint("DefaultLocale")
fun formatHour(hour: Int): String {
    return String.format("%02d.00", hour)
}

@Composable
fun FieldItem(field: Field, selectedDate: String, startTime: Int, endTime: Int, addCart: (Cart) -> Unit) {

    val cartList = remember { mutableStateListOf<Cart>() }

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(vertical = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gambar Lapangan
            Image(
                painter = painterResource(id = R.drawable.sample_field), // Replace with your image resource
                contentDescription = "Field Image",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
            Spacer(modifier = Modifier.width(16.dp))

            // Informasi Lapangan
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = field.name)
                Text(text = selectedDate)
                Text(
                    text = "${formatHour(startTime)} - ${formatHour(endTime)}",
                )
            }
            // Ikon Panah
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add to Cart",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        addCart(
                            Cart(
                                field.name,
                                selectedDate,
                                startTime.toString(),
                                endTime.toString(),
                                field.price.toDouble()
                            )
                        )
                    }
            )
        }
    }
}