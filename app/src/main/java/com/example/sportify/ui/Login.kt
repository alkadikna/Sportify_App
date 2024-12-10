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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

private lateinit var auth: FirebaseAuth
private lateinit var googleSignInClient: GoogleSignInClient


fun LogIn(email: String, pass: String, nContext: Context, navCtrl: NavController){
    auth = Firebase.auth
    auth.signInWithEmailAndPassword(email, pass)
        .addOnCompleteListener{ task ->
            if(task.isSuccessful){
                val user = auth.currentUser?.email
                Toast.makeText(
                    nContext,
                    "Login berhasil",
                    Toast.LENGTH_SHORT
                ).show()
                navCtrl.navigate("home")
            }
            else{
                Toast.makeText(
                    nContext,
                    "Login gagal.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
}

@Composable
fun LoginLayout(
    modifier: Modifier = Modifier,
    navCtrl: NavController
) {
    fun GoogleLogInAuth(account: GoogleSignInAccount?, navCtrl: NavController, activity: Activity) {
        if (account == null) {
            Toast.makeText(activity, "Google Log-In gagal.", Toast.LENGTH_SHORT).show()
            return
        }

        val idToken = account.idToken
        if (idToken == null) {
            Toast.makeText(activity, "Google Log-In gagal.", Toast.LENGTH_SHORT).show()
            return
        }

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        // Cek keberadaan data pengguna di Realtime Database
                        val databaseRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
                        databaseRef.get().addOnSuccessListener { snapshot ->
                            if (snapshot.exists()) {
                                // Data ditemukan, lanjutkan login
                                Toast.makeText(activity, "Google Log-In berhasil", Toast.LENGTH_SHORT).show()
                                navCtrl.navigate("home")
                            } else {
                                // Data tidak ditemukan, batalkan login dan hapus akun dari Firebase Authentication
                                auth.currentUser?.delete()?.addOnCompleteListener { deleteTask ->
                                    if (deleteTask.isSuccessful) {
                                        auth.signOut()
                                        googleSignInClient.signOut()
                                        Toast.makeText(activity, "Akun belum terdaftar. Silakan daftar terlebih dahulu.", Toast.LENGTH_SHORT).show()
                                        navCtrl.navigate("login")
                                    } else {
                                        Toast.makeText(activity, "Gagal menghapus akun dari autentikasi.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }.addOnFailureListener {
                            // Gagal membaca database
                            Toast.makeText(activity, "Terjadi kesalahan saat memeriksa data pengguna.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(activity, "Autentikasi gagal. Silakan coba lagi.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(activity, "Autentikasi gagal.", Toast.LENGTH_SHORT).show()
                }
            }
    }


    var email by remember { mutableStateOf("")}
    var password by remember { mutableStateOf("")}
    val nContext = LocalContext.current

    LaunchedEffect(Unit){
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
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            GoogleLogInAuth(account = account, navCtrl = navCtrl, activity = nContext as Activity)
        } catch (e: ApiException) {
            Log.e("GoogleSignIn", "Google Sign-In gagal: ${e.message}", e)
            Toast.makeText(nContext, "Google Sign-In gagal.", Toast.LENGTH_SHORT).show()
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
                text = "Login",
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
                    Image(painter = painterResource(id = R.drawable.group), contentDescription = null)
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
                value = password,
                onValueChange = {newPass -> password = newPass},
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp),
                leadingIcon = {
                    Image(painter = painterResource(id = R.drawable.group), contentDescription = null)
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
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()){
                        LogIn(email, password, nContext, navCtrl)
                    }
                    else{
                        Toast.makeText(
                            nContext,
                            "Data belum terisi dengan benar",
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
                    text = "LOGIN",
                    color = Color.Black
                )
            }
            Text(
                text = "Lupa Password?",
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .padding(top = 20.dp)
                    .clickable { },
                textAlign = TextAlign.Right
            )
            Text(
                text = "atau masuk dengan",
                Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp)
                    .clickable { },
                textAlign = TextAlign.Center
            )
            Button(
                onClick = {
                    val signInIntent =  googleSignInClient.signInIntent
                    signInLauncher.launch(signInIntent)
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
                    text = "Belum memiliki akun?",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "Register",
                    Modifier
                        .clickable {
                            navCtrl.navigate("register")
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
private fun LoginPrev() {
    SportifyTheme {
        val navCtrlr = rememberNavController()
        LoginLayout(navCtrl = navCtrlr)
    }
}
