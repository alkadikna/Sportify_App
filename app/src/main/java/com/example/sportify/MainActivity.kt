package com.example.sportify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.sportify.Model.InitDb
import com.example.sportify.Model.Order
import com.example.sportify.Repository.deleteOutdatedSchedule
import com.example.sportify.Repository.deleteOutdatedTimes
import com.example.sportify.ui.BookingLayout
import com.example.sportify.ui.HomeLayout
import com.example.sportify.ui.LoginLayout
import com.example.sportify.ui.OrderLayout
import com.example.sportify.ui.ProfileLayout
import com.example.sportify.ui.RegisterLayout
import com.example.sportify.ui.ScheduleLayout
import com.example.sportify.ui.TestingReadDB
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
                    composable("booking") { BookingLayout(navCtrl = navController)}
                    composable("profile") { ProfileLayout(navController = navController, auth = auth) }
                    composable (
                        "result/{fieldType}/{start}/{end}/{selectedDate}",
                        arguments = listOf(
                            navArgument("fieldType") { type = NavType.StringType } ,
                            navArgument("start") { type = NavType.IntType },
                            navArgument("end") { type = NavType.IntType },
                            navArgument("selectedDate") { type = NavType.StringType }
                        )
                    ) {
                        backStackEntry ->
                        val fieldType = backStackEntry.arguments?.getString("fieldType") ?: ""
                        val start = backStackEntry.arguments?.getInt("start") ?: 0
                        val end = backStackEntry.arguments?.getInt("end") ?: 0
                        val selectedDate = backStackEntry.arguments?.getString("selectedDate") ?: ""

                        TestingReadDB(navCtrl = navController, fieldType = fieldType, start = start, end = end, selectedDate)
                    }
                    composable(
                        "order/{cartListJson}",
                    ) {
                        backStackEntry ->
                        val cartListJson = backStackEntry.arguments?.getString("cartListJson")?: "[]"
                        OrderLayout(navCtrl = navController, cartListJson)
                    }
                }
//                InitDb()
                deleteOutdatedSchedule()
                deleteOutdatedTimes()
            }
        }
    }
}

