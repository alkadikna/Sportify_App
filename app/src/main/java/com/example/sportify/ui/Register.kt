package com.example.sportify.ui

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.R
import com.example.sportify.ui.theme.SportifyTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth
private lateinit var googleSignInClient: GoogleSignInClient


fun signup(email: String, username: String, pass: String, nContext: Context, navCtrl: NavController) {
    auth = Firebase.auth
    auth.createUserWithEmailAndPassword(email, pass)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userId = auth.currentUser?.uid
                if (userId != null) {
                    // Menyimpan data default ke Firebase Realtime Database atau Firestore
                    val userData = mapOf(
                        "email" to email,
                        "username" to username,
                        "name" to "-",
                        "profilePhoto" to "default_profile_photo_url", // URL foto profil default
                        "phone" to "-"
                    )
                    FirebaseDatabase.getInstance("https://sportify-3eb54-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("users").child(userId).setValue(userData)
                }

                Toast.makeText(
                    nContext,
                    "Registrasi berhasil",
                    Toast.LENGTH_SHORT
                ).show()
                navCtrl.navigate("home")
            } else {
                Toast.makeText(
                    nContext,
                    "Register gagal.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

fun extractNameAndUsername(displayName: String): Pair<String, String> {
    val regex = Regex("^(.*)\\s\\((.*)\\)$")
    val matchResult = regex.find(displayName)

    return if (matchResult != null) {
        val name = matchResult.groupValues[1].trim()
        val username = matchResult.groupValues[2].trim()
        name to username
    } else {
        val name = displayName.trim()
        val username = name.split(" ").first()
        name to username
    }
}

@Composable
fun RegisterLayout(
    modifier: Modifier = Modifier,
    navCtrl: NavController
    ) {
    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    var repeatPass by remember { mutableStateOf("")}
    var username by remember { mutableStateOf("") }
    val nContext = LocalContext.current

    fun GoogleSignInAuth(account: GoogleSignInAccount, navCtrl: NavController, activity: Activity) {
        if (account != null) {
            val idToken = account.idToken
            if (idToken != null) {
                val credential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(activity) { tasks ->
                        if (tasks.isSuccessful) {
                            val userId = auth.currentUser?.uid
                            val displayName = account.displayName ?: "-"

                            // Memisahkan nama dan username
                            val (name, username) = extractNameAndUsername(displayName)

                            val userData = mapOf(
                                "email" to (account.email ?: "-"),
                                "username" to username,
                                "name" to name,
                                "profilePhoto" to (account.photoUrl?.toString() ?: "default_profile_photo_url"),
                                "phone" to "-"
                            )

                            FirebaseDatabase.getInstance().getReference("users").child(userId!!).setValue(userData)

                            navCtrl.navigate("home")
                        } else {
                            Toast.makeText(activity, "Autentikasi Gagal.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(activity, "Google Sign-In gagal.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(activity, "Sign-In gagal.", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        auth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(nContext.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(nContext, gso)
    }


    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            GoogleSignInAuth(account = task.result, navCtrl = navCtrl, activity = nContext as Activity)
        } catch (e : Exception) {
            Toast.makeText(nContext, "Register Google Gagal. Tolong register dengan benar", Toast.LENGTH_SHORT).show()
        }

    }

    Box (
        Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1A96F8),
                        Color(0xFFA9CDFC)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ){
        Image(
            painter = painterResource(id = R.drawable.fad60d1e_637a_48cd_b76f_088bfa2fd3d9_1),
            contentDescription = null,
            Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,

            )
        Column {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = null,
                Modifier
                    .padding(60.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "Register",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp),
                fontSize = 30.sp
            )
            TextField(
                value = email,
                onValueChange = {newEmail -> email = newEmail},
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.email_symbol), contentDescription = null)
                },
                label = { Text(text = "Email")},
                placeholder = { Text(text = "Masukkan Email Anda")},
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            TextField(
                value = username,
                onValueChange = { newUsername -> username = newUsername },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.group), contentDescription = null)
                },
                label = { Text(text = "Username") },
                placeholder = { Text(text = "Masukkan Username Anda") },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                )
            )
            TextField(
                value = password,
                onValueChange = {newPass -> password = newPass},
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.icon_password), contentDescription = null)
                },
                label = { Text(text = "Password")},
                placeholder = { Text(text = "Masukkan Password Anda")},
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            TextField(
                value = repeatPass,
                onValueChange = {newPass -> repeatPass = newPass},
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.icon_password), contentDescription = null)
                },
                label = { Text(text = "Repeat Password")},
                placeholder = { Text(text = "Masukkan Ulang Password Anda")},
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = {
                    if(email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty()){
                        if(password == repeatPass){
                            signup(email, username, password, nContext, navCtrl)
                        }
                        else{
                            Toast.makeText(
                                nContext,
                                "Tolong ulangi password dengan benar",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    else{
                        Toast.makeText(
                            nContext,
                            "Tolong isi semua data dengan benar",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(Color(0xFFBEF0FF))

            ) {
                Text(
                    text = "REGISTER",
                    color = Color.Black
                )
            }
            Text(
                text = "atau daftar dengan",
                Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                    .clickable { },
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    if (signInLauncher != null) {
                        val signInIntent = googleSignInClient.signInIntent
                        signInLauncher?.launch(signInIntent)
                    }
                },
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 50.dp)
                    .padding(top = 10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                contentPadding = PaddingValues(2.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.flat_color_icons_google),
                    contentDescription = null,
                    Modifier
                        .width(35.dp)
                        .height(40.dp)
                )
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                Text(
                    text = "Continue With Google",
                    fontSize = 18.sp,
                    color = Color.LightGray
                )
            }
            Row(
                Modifier
                    .padding(top = 40.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Sudah memiliki akun?",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Login",
                    Modifier
                        .clickable {
                            navCtrl.navigate("login")
                        },
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    textDecoration = TextDecoration.Underline
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterPrev() {
    SportifyTheme {
        val navCtrlr = rememberNavController()
        RegisterLayout(navCtrl = navCtrlr)
    }
}
