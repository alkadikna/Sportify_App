package com.example.sportify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sportify.ui.theme.SportifyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = Firebase.auth
        val currentUser = auth.currentUser
        val startDestination = if (currentUser != null) "home" else "login"
        setContent {
            SportifyTheme {
                val navController =  rememberNavController()
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("login") { LoginLayout(navCtrl = navController) }
                    composable("register") { RegisterLayout(navCtrl = navController) }
                    composable("home") { HomeLayout(modifier = Modifier, navCtrl = navController) }
                    composable("schedule") { ScheduleLayout(navCtrl = navController) }
                }
            }
        }
    }
}

