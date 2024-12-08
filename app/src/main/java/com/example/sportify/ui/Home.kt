package com.example.sportify.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sportify.Model.Time
import com.example.sportify.R
import com.example.sportify.Repository.getOrder
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.TopSection
import com.example.sportify.ui.theme.SportifyTheme

private lateinit var auth: FirebaseAuth;
private lateinit var googleSignInClient: GoogleSignInClient
private lateinit var database: FirebaseDatabase;

@Composable
fun HomeLayout(modifier: Modifier = Modifier, navCtrl: NavController) {
    Scaffold(
        topBar = { FloatingSearchBarLayout() },
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
                    item { FieldTypeSection() }
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
fun FloatingSearchBarLayout() {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopSection()
        SearchBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 25.dp)
        )
    }
}


@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
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
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
        }
    }
}

@Composable
fun FieldTypeSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Pilih Jenis Lapangan",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            items(listOf("Badminton", "Futsal", "Tennis", "Basket", "Soon")) { type ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(vertical = 10.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        modifier = Modifier.size(64.dp),
                        color = Color.Gray // Placeholder for image background color
                    ) {
                        // Replace this with an Image composable for actual images
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = type.take(1),
                                fontSize = 20.sp,
                                color = Color.White
                            ) // Placeholder text (e.g., "B" for "Badminton")
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
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
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
        orderList.forEach { order ->
            Surface(
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                color = Color.LightGray
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
                        contentDescription = "Arrow Icon"
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeLayoutPreview(){
    SportifyTheme {
        HomeLayout(modifier = Modifier, navCtrl = rememberNavController())
    }
}