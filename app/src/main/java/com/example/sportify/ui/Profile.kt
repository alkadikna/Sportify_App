package com.example.sportify.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.R

@Composable
fun ProfileLayout(navController: NavController) {
    Scaffold(
        topBar = {
            //EmptyTopSection()
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                index = 4 //
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            ProfilePhotoSection(
                name = "Sukimin Sukasmin",
                username = "@crazikiller",
            )

            Spacer(modifier = Modifier.height(20.dp))

            ProfileInfoSection(
                name = "Sukimin Sukasmin",
                username = "@crazikiller",
                email = "orangtanvan@gmail.com",
                phone = "+628123456789"
            )

            // Pengaturan
            Section(
                title = "Pengaturan",
                items = listOf(
                    Pair("Edit Profile", R.drawable.ic_baseline_account_box),
                    Pair("Privasi", R.drawable.dashicons_privacy),
                    Pair("Ganti Bahasa", R.drawable.mdi_language)
                )
            )

            // Pembayaran
            Section(
                title = "Pembayaran",
                items = listOf(
                    Pair("Opsi Pembayaran", R.drawable.wallet),
                    Pair("Daftar Transaksi", R.drawable.terms_condition)
                )
            )

            //Tentang
            Section(
                title = "Tentang",
                items = listOf(
                    Pair("Syarat dan Ketentuan", R.drawable.ph_note_fill),
                    Pair("Kebijakan Privasi", R.drawable.ic_baseline_privacy_tip),
                    Pair("Versi Aplikasi", R.drawable.version_app)
                )
            )
        }
    }
}

@Composable
fun ProfilePhotoSection(name: String, username: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Blue background at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFF2196F3))
        )

        // White profile card
        Surface(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 10.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp, bottom = 24.dp)
            ) {
                // Nama dan Username
                Text(
                    text = name,
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = username,
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                    )
                )
            }
        }

        // Profile image positioned between the blue background and white card
        Image(
            painter = painterResource(id = R.drawable.default_picture), // Replace with your profile picture
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopCenter) // Align image in the center at the top of the card
                .offset(y = 20.dp) // Adjust offset to position image between blue background and card
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

@Composable
fun ProfileInfoSection(name: String, username: String, email: String, phone: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(0.1.dp, color = Color.Gray, RoundedCornerShape(5.dp))


    )
    {
        ProfileInfoItem(label = "Nama", value = name)
        ProfileInfoItem(label = "Username", value = username)
        ProfileInfoItem(label = "E-mail", value = email)
        ProfileInfoItem(label = "Telp", value = phone)
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) {
    Column(modifier = Modifier.padding(12.dp)) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(40.dp)
                .border(0.1.dp, color = Color.Gray, RoundedCornerShape(5.dp))
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                )
            )
        }

    }
}

@Composable
fun Section(title: String, items: List<Pair<String, Int>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.1.dp, color = Color.Gray, RoundedCornerShape(5.dp))
        ) {
            items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = item.second),
                        contentDescription = item.first,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = item.first,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.weui_arrow_filled),
                        contentDescription = null,
                        tint = Color.Gray
                    )
                }
                HorizontalDivider(thickness = 0.2.dp)
            }
        }
    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    val navController = rememberNavController()
    ProfileLayout(navController = navController)
}

