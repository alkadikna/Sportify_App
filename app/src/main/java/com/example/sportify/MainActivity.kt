package com.example.sportify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sportify.Model.InitDb
import com.example.sportify.ui.BookingLayout
import com.example.sportify.ui.HomeLayout
import com.example.sportify.ui.LoginLayout
import com.example.sportify.ui.RegisterLayout
import com.example.sportify.ui.ScheduleLayout
import com.example.sportify.ui.theme.SportifyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth
private lateinit var database: FirebaseDatabase


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
        val currentUser = auth.currentUser

        //initialize database
//        InitDb()

        val startDestination = if (currentUser != null) "home" else "login"
        setContent {
            SportifyTheme {
                val navController =  rememberNavController()
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") { LoginLayout(navCtrl = navController) }
                    composable("register") { RegisterLayout(navCtrl = navController) }
                    composable("home") { HomeLayout(modifier = Modifier, navCtrl = navController) }
                    composable("schedule") { ScheduleLayout(navCtrl = navController) }
                    composable("booking") { BookingLayout(navCtrl = navController)}


                }

            }
        }
    }
}

