package com.example.sportify.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileLayout(navController: NavController, auth: FirebaseAuth) {
    val currentUser = auth.currentUser

//    val context = LocalContext.current
//
//    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(context.getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build()
//
//    googleSignInClient = GoogleSignIn.getClient(context, gso)

    Scaffold(
        topBar = {
            //EmptyTopSection()
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                index = 4
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (currentUser != null) {
                ProfilePhotoSection(
                    name = "Sukimin Sukasmin",
                    username = currentUser.email.toString(),
                    profilePhotoUrl = null
                )

                Spacer(modifier = Modifier.height(20.dp))

                ProfileInfoSection(
                    name = "Sukasmin",
                    username = currentUser.displayName.toString(),
                    email = currentUser.email.toString(),
                    phone = currentUser.phoneNumber.toString()
                )

                // Pengaturan
                Section(
                    title = "Pengaturan",
                    items = listOf(
                        Triple("Edit Profile", R.drawable.ic_baseline_account_box, ::editProfile),
                        Triple("Privasi", R.drawable.dashicons_privacy, ::privasi),
                        Triple("Ganti Bahasa", R.drawable.mdi_language, ::gantiBahasa)
                    )
                )

                // Pembayaran
                Section(
                    title = "Pembayaran",
                    items = listOf(
                        Triple("Opsi Pembayaran", R.drawable.wallet, null),
                        Triple("Daftar Transaksi", R.drawable.terms_condition, null)
                    )
                )

                //Tentang
                Section(
                    title = "Tentang",
                    items = listOf(
                        Triple("Syarat dan Ketentuan", R.drawable.ph_note_fill, null),
                        Triple("Kebijakan Privasi", R.drawable.ic_baseline_privacy_tip, null),
                        Triple("Versi Aplikasi", R.drawable.version_app, null)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                //Log Out
                LogoutButton(navController, auth/*, googleSignInClient*/)
            }
        }
    }
}

//Profil
@Composable
fun ProfilePhotoSection(name: String?, username: String?, profilePhotoUrl: String?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Blue background at the top
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Color(0xFF2196F3))
        )

        // White profile card
        Surface(
            modifier = Modifier
                .padding(top = 60.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = 10.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 40.dp, bottom = 24.dp)
            ) {
                Text(
                    text = name ?: "-",
                    style = TextStyle(
                        fontSize = 20.sp,
                        color = Color.Black,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = username ?: "-",
                    style = TextStyle(
                        fontSize = 14.sp,
                        color = Color.Gray,
                        fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                    )
                )
            }
        }

        // Profile image
        Image(
            painter = painterResource(id = profilePhotoUrl?.toInt() ?: R.drawable.default_picture),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopCenter)
                .offset(y = 20.dp)
                .clip(CircleShape)
                .border(2.dp, Color.White, CircleShape)
        )
    }
}

@Composable
fun ProfileInfoSection(name: String, username: String, email: String, phone: String) { //Informasi akun
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .border(0.1.dp, color = Color.Gray, RoundedCornerShape(5.dp))


    )
    {
        ProfileInfoItem(label = "Nama", value = name)
        ProfileInfoItem(label = "Username", value = username)
        ProfileInfoItem(label = "E-mail", value = email)
        ProfileInfoItem(label = "Telp", value = phone)
    }
}

@Composable
fun ProfileInfoItem(label: String, value: String) { //Desain per informasi akun
    Column(modifier = Modifier.padding(12.dp)) {
        Text(
            text = label,
            style = TextStyle(
                fontSize = 14.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .size(40.dp)
                .border(0.1.dp, color = Color.Gray, RoundedCornerShape(5.dp))
                .padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                style = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontFamily = FontFamily(Font(R.font.inria_serif_bold))
                )
            )
        }

    }
}

//Opsi Fitur except Log Out
@Composable
fun Section(title: String, items: List<Triple<String, Int, (() -> Unit)?>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold))
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(0.1.dp, color = Color.Gray, RoundedCornerShape(5.dp))
        ) {
            items.forEach { item ->
                Button(
                    onClick = { item.third?.invoke() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(Color.Transparent)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = item.second),
                            contentDescription = item.first,
                            modifier = Modifier.size(20.dp),
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = item.first,
                            fontSize = 14.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.weui_arrow_filled),
                            contentDescription = null,
                            tint = Color.Gray
                        )
                    }
                }
                HorizontalDivider(thickness = 0.2.dp)
            }
        }
    }
}

//Fungsi log out
@Composable
fun LogoutButton(navController : NavController, auth: FirebaseAuth/*, googleSignInClient: GoogleSignInClient*/) {
    val context = LocalContext.current

    Button(
        onClick = {
            try {
                auth.signOut()
                //googleSignInClient.signOut()
                navController.navigate("login")
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(250, 64, 50)),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_logout),
                contentDescription = "Logout Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Keluar",
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.inria_serif_bold)),
                fontSize = 18.sp
            )
        }
    }
}

//fungsi-fungsi fitur
fun editProfile() {

}

fun privasi() {

}

fun gantiBahasa() {

}
//@Preview
//@Composable
//fun ProfileScreenPreview() {
//    val navController = rememberNavController()
//    val user = User(
//        username = "@crazikiller",
//        email = "orangtanvan@gmail.com",
//        password = "password", // contoh saja, di real app jangan menyimpan password plaintext
//        name = "Sukimin Sukasmin",
//        phone = "+628123456789",
//        profilePhotoUrl = null // atau URL dari foto profil jika ada
//    )
//    ProfileLayout(navController = navController)
//}

