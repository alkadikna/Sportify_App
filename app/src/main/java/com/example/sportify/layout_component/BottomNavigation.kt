package com.example.sportify.layout_component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportify.R

@Composable
public fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavController, index: Int) {
    var selectedIndex by remember { mutableStateOf(index) }

    BottomNavigation(
        modifier = modifier
            .height(70.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .shadow(4.dp, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .border(1.dp, color = Color.Gray, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        backgroundColor = Color.White,
        elevation = 8.dp
    ) {
        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_home), // Replace with your resource
                    contentDescription = "Home",
                    tint = if (selectedIndex == 0) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            label = {
                Text(
                    text = "Beranda",
                    fontSize = 12.sp,
                    color = if (selectedIndex == 0) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
                navController.navigate("home")
                      },
            modifier = Modifier.padding(top = 5.dp)
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calender), // Replace with your resource
                    contentDescription = "Schedule",
                    tint = if (selectedIndex == 1) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            label = {
                Text(
                    text = "Jadwal",
                    fontSize = 12.sp,
                    color = if (selectedIndex == 1) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
                navController.navigate("schedule")
                      },
            modifier = Modifier.padding(top = 5.dp)
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_book), // Replace with your resource
                    contentDescription = "Bookings",
                    tint = if (selectedIndex == 2) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            label = {
                Text(
                    text = "Reservasi",
                    fontSize = 11.sp,
                    color = if (selectedIndex == 2) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            selected = selectedIndex == 2,
            onClick = { selectedIndex = 2 },
            modifier = Modifier.padding(top = 5.dp)
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bell), // Replace with your resource
                    contentDescription = "Notifications",
                    tint = if (selectedIndex == 3) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            label = {
                Text(
                    text = "Notifikasi",
                    fontSize = 11.sp,
                    color = if (selectedIndex == 3) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            selected = selectedIndex == 3,
            onClick = { selectedIndex = 3 },
            modifier = Modifier.padding(top = 5.dp)
        )

        BottomNavigationItem(
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person), // Replace with your resource
                    contentDescription = "Akun",
                    tint = if (selectedIndex == 4) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            label = {
                Text(
                    text = "Akun",
                    fontSize = 12.sp,
                    color = if (selectedIndex == 4) colorResource(id = R.color.main_blue) else Color.Black
                )
            },
            selected = selectedIndex == 4,
            onClick = {
                selectedIndex = 4
                navController.navigate("profile")},
            modifier = Modifier.padding(top = 5.dp)
        )
    }
}

