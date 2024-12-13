package com.example.sportify.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Scaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportify.R
import com.example.sportify.layout_component.TopSection
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun EditProfileLayout(navCtrl: NavController, auth: FirebaseAuth) {
    var name by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    val context = LocalContext.current

    val currentUser = auth.currentUser
    val userId = currentUser?.uid

    val database = FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app")
    val userRef = userId?.let { database.getReference("users").child(it) }

    fun fetchUserData(userRef: DatabaseReference) {
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").getValue(String::class.java) ?: "-"
                username = snapshot.child("username").getValue(String::class.java) ?: "-"
                phone = snapshot.child("phone").getValue(String::class.java) ?: "-"
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ProfileLayout", "Failed to fetch user data: ${error.message}")
            }
        })
    }

    LaunchedEffect(userRef) {
        if (userRef != null) {
            fetchUserData(userRef)
        }
    }

    fun saveUserData() {
        if (userId != null) {
            val userData = mapOf(
                "name" to name,
                "username" to username,
                "phone" to phone
            )
            userRef?.updateChildren(userData)?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Profil berhasil diperbarui!", Toast.LENGTH_SHORT).show()
                    navCtrl.navigate("profile")
                } else {
                    Toast.makeText(context, "Gagal memperbarui profil: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(context, "Pengguna tidak ditemukan.", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = { TopSection() },
        bottomBar = {}
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Edit Profile",
                        style = TextStyle(
                            fontSize = 30.sp,
                            color = Color.Black,
                            fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        ),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(vertical = 10.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Input fields
                    InputField(label = "Name", value = name, onValueChange = { name = it })
                    Spacer(modifier = Modifier.height(20.dp))
                    InputField(label = "Username", value = username, onValueChange = { username = it })
                    Spacer(modifier = Modifier.height(20.dp))
                    InputField(label = "Phone", value = phone, onValueChange = { phone = it }, keyboardType = KeyboardType.Number)

                    Button(
                        onClick = { saveUserData() },
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(Color(90, 181, 255))
                    ) {
                        Text(
                            text = "Edit",
                            style = TextStyle(
                                fontSize = 15.sp,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.poppins_semi_bold)),
                            ),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
            )
        )
        Spacer(modifier = Modifier.height(7.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            Modifier
                .fillMaxWidth()
                .border(0.2.dp, color = Color.Gray, RoundedCornerShape(5.dp)),
            textStyle = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
            ),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
        )
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun EditProfilePreview() {
//    EditProfile()
//}