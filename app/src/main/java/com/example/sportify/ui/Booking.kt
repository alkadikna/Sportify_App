package com.example.sportify.ui

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.TopSection
import com.example.sportify.layout_component.TopSectionWithImage
import com.example.sportify.ui.theme.SportifyTheme
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private lateinit var auth: FirebaseAuth;
private lateinit var googleSignInClient: GoogleSignInClient
private lateinit var database: FirebaseDatabase;

@Composable
fun BookingLayout(modifier: Modifier = Modifier, navCtrl: NavController) {
    Scaffold(
        topBar = { FloatingForm() },
        bottomBar = {
            BottomNavigationBar(
                navController = navCtrl,
                index = 0
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .size(150.dp)
                    .padding(paddingValues)

            ) {
            }
        }
    )
}

@Composable
fun FloatingForm(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopSectionWithImage()
        Form(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 25.dp)
        )
    }
}

@Composable
fun Form(modifier: Modifier = Modifier) {
    Column(
        Modifier
        .shadow(
            8.dp,
            shape = RoundedCornerShape(24.dp),
            ambientColor = Color.Black.copy(0.2f),
            spotColor = Color.Black.copy(0.2f)
        )
    ){

    }
}

@Composable
fun ModularDropDown(modifier: Modifier, anyList: List<Any>) {
    val context = LocalContext.current
    val listSport = arrayOf("Badminton", "Tennis", "Futsal", "Basket")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Cabang Olahraga") }

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .shadow(
                    4.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(0.2f),
                    spotColor = Color.Black.copy(0.2f)
                )
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = selectedText,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

//        AnimatedVisibility(visible = expanded) {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(
//                        8.dp,
//                        shape = RoundedCornerShape(16.dp),
//                        ambientColor = Color.Black.copy(0.2f),
//                        spotColor = Color.Black.copy(0.2f)
//                    )
//                    .background(Color.White, shape = RoundedCornerShape(16.dp))
//                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
//            ) {
//                anyList.forEach { item ->
//                    Text(
//                        text = item,
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable {
//                                selectedText = item
//                                expanded = false
//                                Toast
//                                    .makeText(context, item, Toast.LENGTH_SHORT)
//                                    .show()
//                            }
//                            .padding(16.dp)
//                    )
//                }
//            }
//        }
    }
}

@Preview(showBackground = true)
@Composable
fun BookingLayoutPreview(){
    SportifyTheme {
        BookingLayout(modifier = Modifier, navCtrl = rememberNavController())
    }
}