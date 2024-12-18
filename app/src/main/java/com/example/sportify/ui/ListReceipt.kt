package com.example.sportify.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportify.Model.Receipt
import com.example.sportify.R
import com.example.sportify.Repository.fetchUserListReceipt
import com.example.sportify.layout_component.TopSection
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ReceiptListScreen(navCtrl: NavController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser
    val receiptList = remember { mutableStateOf<List<Pair<String, Receipt>>>(emptyList()) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    LaunchedEffect(currentUser) {
        currentUser?.uid?.let { uid ->
            fetchUserListReceipt(
                uid,
                onSuccess = { receipts ->
                    receiptList.value = receipts
                },
                onError = { error ->
                    errorMessage.value = error
                }
            )
        }
    }

    Scaffold(
        topBar = {
            TopSection()

            IconButton(
                onClick = { navCtrl.navigate("profile") },
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back_arrow),
                    contentDescription = "back",
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color.White)
        ) {
            if (errorMessage.value != null) {
                Text(
                    text = "Error: ${errorMessage.value}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            } else if (receiptList.value.isEmpty()) {
                Text(
                    text = "Tidak ada struk.",
                    style = TextStyle(
                        fontSize = 22.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                    ),
                    modifier = Modifier
                        .padding(20.dp)
                        .align(Alignment.CenterHorizontally)
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(receiptList.value) { (receiptKey, receipt) ->
                        ReceiptItem(receipt, receiptKey, navCtrl)
                    }
                }
            }
        }
    }
}


@Composable
fun ReceiptItem(receipt: Receipt, receiptKey: String, navCtrl: NavController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navCtrl.navigate("receipt/$receiptKey")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "ID Pesanan: ${receipt.orderId}",
                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Dipesan Pada: ${receipt.orderTime}",
                style = TextStyle(fontSize = 14.sp, color = Color.Black)
            )
        }
    }
    Spacer(modifier = Modifier.height(15.dp))
}


