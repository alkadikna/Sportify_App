package com.example.sportify.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material.Surface
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sportify.Model.Time
import com.example.sportify.R
import com.example.sportify.Repository.FetchScheduleData
import com.example.sportify.Repository.getOrder
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.TopSection
import com.example.sportify.ui.theme.SportifyTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay

private lateinit var auth: FirebaseAuth;
private lateinit var googleSignInClient: GoogleSignInClient
private lateinit var database: FirebaseDatabase;

@Composable
fun HomeLayout(navCtrl: NavController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    var userName by remember { mutableStateOf("-") }
    fun fetchUserData(uid: String) {
        val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(uid)
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userName = snapshot.child("username").getValue(String::class.java) ?: "-"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("HomeLayout", "Failed to fetch username data: ${error.message}")
            }
        })
    }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { fetchUserData(it) }
    }

    Scaffold(
        topBar = { FloatingSearchBarLayout(userName) },
        bottomBar = {
            BottomNavigationBar(
                navController = navCtrl,
                index = 0
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .padding(top = 20.dp)
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // Field types section
                    item { FieldTypeSection(navCtrl) }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // Available fields section
                    item { AvailableFieldsSection() }
                    item { Spacer(modifier = Modifier.height(16.dp)) }

                    // User's booking section
                    item { UserBookingSection() }
                }
            }
        }
    )
}

@Composable
fun FloatingSearchBarLayout(userName : String) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopSection()
        WelcomeBox(userName = userName,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 60.dp)
        )
    }
}


@Composable
fun WelcomeBox(userName: String, modifier: Modifier = Modifier) {
    var displayedText by remember { mutableStateOf("") }
    val fullText = "Ayo kita berolahraga, $userName!"

    LaunchedEffect(fullText) {
        for (i in fullText.indices) {
            displayedText = fullText.substring(0, i + 1)
            delay(100)
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .height(48.dp)
            .shadow(
                8.dp,
                shape = RoundedCornerShape(24.dp),
                ambientColor = Color.Black.copy(0.2f),
                spotColor = Color.Black.copy(0.2f)
            )
            .background(Color.White, shape = RoundedCornerShape(24.dp))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = displayedText,
            style = androidx.compose.material.MaterialTheme.typography.body1.copy(
                color = Color.Gray
            )
        )
    }
}

@Composable
fun FieldTypeSection(navCtrl: NavController) {

    val calendar = remember { Calendar.getInstance() }
    val selectedDate = remember {
        mutableStateOf(
            SimpleDateFormat("dd-MM-yyyy", Locale("id","ID")).format(calendar.time)
        )
    }
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
    selectedDate.value = dateFormat.format(calendar.time)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Pilih Jenis Lapangan",
            style = MaterialTheme.typography.bodyLarge
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            val fieldTypes = listOf(
                "Badminton" to R.drawable.bultang,
                "Futsal" to R.drawable.futsal,
                "Tennis" to R.drawable.tennis,
                "Basket" to R.drawable.basket,
                "Soon" to null // placeholder
            )

            items(fieldTypes) { (type, imageRes) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    if (imageRes != null) {
                       Button(
                           onClick = {
                               navCtrl.navigate("result/$type/0/0/${selectedDate.value}")
                           },
                           modifier = Modifier
                               .size(64.dp)
                               .clip(CircleShape)
                               .border(1.dp, Color.Gray, CircleShape)
                       ) {
                           Image(
                              painter = painterResource(id = imageRes),
                              contentDescription = "$type Image",
                              modifier = Modifier
                                  .size(64.dp)
                                  .clip(CircleShape),
                              contentScale = ContentScale.Crop
                           )
                       }
                    } else {
                        // placeholder
                        Surface(
                            shape = CircleShape,
                            modifier = Modifier.size(64.dp),
                            color = Color.Gray
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "S",
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = type,
                        fontSize = 12.sp,
                        color = Color.Black
                    )
                }
            }
        }
    }
}



@Composable
fun AvailableFieldsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = "Lapangan yang tersedia",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            val fields = listOf("Badminton A", "Tennis A", "Lapangan Basket")

            items(fields.size) { index ->
                Card(
                    modifier = Modifier
                        .width(150.dp)
                        .height(200.dp),
                    elevation = CardDefaults.elevatedCardElevation(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.badminton),
                            contentDescription = null,
                            modifier = Modifier
                                .height(90.dp)
                                .fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = fields[index],
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
                        )
                        Text(
                            text = "16.00-18.00",
                            fontSize = 15.sp,
                            color = Color.Gray,
                            modifier = Modifier.padding(5.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserBookingSection() {
    val orderList = remember { mutableStateListOf<Cart>() }

    LaunchedEffect(Unit) {
        getOrder { orders ->
            orderList.clear()  // Clear previous orders
            orderList.addAll(orders)  // Add new orders
        }
    }

    Column {
        Text(text = "Pesanan anda", style = MaterialTheme.typography.bodyLarge)

        Spacer(modifier = Modifier.height(8.dp))

        // Sample bookings
//        listOf(
//            "Lapangan Badminton A3, Selasa 03 Okt 2023, 16:00 - 18:00",
//            "Lapangan Badminton B1, Selasa 03 Okt 2023, 14:00 - 16:00",
//            "Lapangan Badminton B1, Selasa 03 Okt 2023, 14:00 - 16:00",
//            "Lapangan Badminton B1, Selasa 03 Okt 2023, 14:00 - 16:00",
//            "Lapangan Badminton B1, Selasa 03 Okt 2023, 14:00 - 16:00",
//            "Lapangan Badminton B1, Selasa 03 Okt 2023, 14:00 - 16:00",
//            "Lapangan Badminton B1, Selasa 03 Okt 2023, 14:00 - 16:00",
//        ).
        Log.d("OrderList", "Orderlist: $orderList")
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        orderList.forEach { order ->
            val targetTime = (order.endTime.toInt() * 60)
            val currentTime = (currentHour * 60) + currentMinute
            if(currentTime < targetTime){
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    color = Color.White,
                    elevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sample_field), // Replace with your image resource
                            contentDescription = "Field Image",
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(text = order.name, fontSize = 14.sp)
                            Text(text = order.date, fontSize = 14.sp)
                            Text(text = formatHour(order.startTime.toInt()) + "-" + formatHour(order.endTime.toInt()), fontSize = 14.sp)
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowRight, // Replace with your arrow icon resource
                            contentDescription = "Arrow Icon",
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeLayoutPreview(){
    SportifyTheme {
        HomeLayout(navCtrl = rememberNavController(), auth)
    }
}