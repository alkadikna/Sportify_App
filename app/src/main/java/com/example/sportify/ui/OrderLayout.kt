package com.example.sportify.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.sportify.Repository.saveOrder
import com.example.sportify.Repository.updateField
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

private lateinit var database: FirebaseDatabase

@Composable
fun OrderLayout(navCtrl: NavController, cartListJson: String, modifier: Modifier = Modifier) {
    
    database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")

    val gson = Gson()
    val listType = object : TypeToken<List<Cart>>() {}.type
    val cartList: List<Cart> = gson.fromJson(cartListJson, listType)
    
    Column {
        LazyColumn {
            items(cartList){ item ->
                Text(text = item.date)
                Text(text = item.startTime + "-" + item.endTime)
                Text(text = item.name)
                Text(text = item.price.toString())
            }
        }
        Button(
            onClick = { updateField(cartList)
                navCtrl.navigate("home")
                saveOrder(cartList)
            }

        ) {
            Text(text = "Pesan")
        }
    }
}