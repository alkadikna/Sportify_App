package com.example.sportify.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportify.Model.Field
import com.example.sportify.Model.Time
import com.example.sportify.R
import com.example.sportify.Repository.FetchScheduleData
import com.example.sportify.Repository.getScheduleByTime
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.FloatingCartButton
import com.example.sportify.layout_component.TopSection
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

    Scaffold(
        topBar = { TopSection() },
        bottomBar = {},
        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(timeList) { time ->
                        time.fieldList.filter { it.name.contains("", ignoreCase = true) }
                            .forEach { field ->
                                if (field.isAvailable) {
                                    FieldItem(
                                        field = field,
                                        selectedDate = selectedDate,
                                        startTime = time.startTime,
                                        endTime = time.endTime,
                                        addCart = { item ->
                                            cartList.add(item)
                                        },
                                        removeCart = { item ->
                                            cartList.remove(item)
                                        }
                                    )
                                }
                            }
                    }
                }

                FloatingCartButton(
                    onClick = { showDialog = true },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp)
                )
                ShowCartDialog(
                    showDialog = showDialog,
                    onDismissDialog = { showDialog = false },
                    cart = cartList,
                    navCtrl = navCtrl
                )
            }
        }
    )
}

@SuppressLint("DefaultLocale")
fun formatHour(hour: Int): String {
    return String.format("%02d.00", hour)
}

@Composable
fun FieldItem(field: Field, selectedDate: String, startTime: Int, endTime: Int, addCart: (Cart) -> Unit, removeCart: (Cart) -> Unit) {
    val isAdded = remember { mutableStateOf(false) }
    val cartItem = Cart(
        field.name,
        selectedDate,
        startTime.toString(),
        endTime.toString(),
        field.price.toDouble()
    )

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp)
            .padding(vertical = 10.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
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
            // Ikon
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(if (isAdded.value) colorResource(id = R.color.main_blue) else Color.Transparent)
                    .clickable {
                        if (isAdded.value) {
                            removeCart(cartItem)
                        } else {
                            addCart(cartItem)
                        }
                        isAdded.value = !isAdded.value
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isAdded.value) Icons.Default.Check else Icons.Default.Add,
                    contentDescription = "Toggle Cart",
                    tint = if (isAdded.value) Color.White else Color.Black,
                    modifier = if (isAdded.value) Modifier.size(16.dp) else Modifier.size(24.dp)
                )
            }
        }
    }
}
