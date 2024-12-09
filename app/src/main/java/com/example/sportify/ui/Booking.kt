package com.example.sportify.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.BottomSheet
import com.example.sportify.layout_component.FloatingCartButton
import com.example.sportify.layout_component.TopSectionWithImage
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

private lateinit var auth: FirebaseAuth
private lateinit var googleSignInClient: GoogleSignInClient
private lateinit var database: FirebaseDatabase

@Composable
fun BookingLayout(modifier: Modifier = Modifier, navCtrl: NavController) {
    // State for selected date
    val calendar = remember { Calendar.getInstance() }
    val selectedDate = remember {
        mutableStateOf(
            SimpleDateFormat("dd-MM-yyyy", Locale("id","ID")).format(calendar.time)
        ) }

    val context = LocalContext.current

    // Function to update date
    fun updateDate(action: String) {
        when (action) {
            "previous" -> calendar.add(Calendar.DAY_OF_MONTH, -1)
            "next" -> calendar.add(Calendar.DAY_OF_MONTH, 1)
            "select" -> { /* Handled by DatePicker */ }
        }
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        selectedDate.value = dateFormat.format(calendar.time)
    }

    LaunchedEffect(Unit) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))
        calendar.time = dateFormat.parse(selectedDate.value) ?: Calendar.getInstance().time
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                navController = navCtrl,
                index = 2
            )
        },
//        floatingActionButton = { FloatingCartButton() },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                TopSectionWithImage()
                Form(
                    modifier = Modifier,
                    navCtrl = navCtrl,
                    selectedDate = selectedDate,
                    onDateChange = { action ->
                        if (action == "select") {
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    calendar.set(year, month, dayOfMonth)
                                    updateDate("") // Reformat date after selection
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
                        } else {
                            updateDate(action) // Previous or Next
                        }
                    }
                )
//                FloatingCartButton()
            }
        }
    )
}

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(
    modifier: Modifier = Modifier,
    navCtrl: NavController,
    selectedDate: MutableState<String>,
    onDateChange: (String) -> Unit
) {
    var selectedActivity by remember { mutableStateOf("Aktivitas") }
    var selectedSport by remember { mutableStateOf("Cabang Olahraga") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var nContext = LocalContext.current
    var isValid by remember { mutableStateOf(false)}

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            BottomSheet { sport ->
                selectedSport = sport
//                scope.launch { scaffoldState.bottomSheetState.hide() }
            }
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp,

            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .padding(top = 0.dp)
                .padding(horizontal = 20.dp)
//                .offset(y = (-50).dp)
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .background(Color.White)
            ) {
                DropdownItem(
                    label = selectedSport,
                    onClick = {
                        scope.launch { scaffoldState.bottomSheetState.expand() }
                    }
                )
                DropdownItem(
                    label = selectedDate.value,
                    onClick = { onDateChange("select") }
                )
                TextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    Modifier
                        .padding(12.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .height(60.dp)
                        .fillMaxWidth(),
                    label = { Text("Waktu Mulai") },
                    placeholder = { Text("Contoh: 10") },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.White
                    )
                )
                TextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    Modifier
                        .padding(12.dp)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .height(60.dp)
                        .fillMaxWidth(),
                    label = { Text("Waktu Selesai") },
                    placeholder = { Text("Contoh: 12") },
                    colors = TextFieldDefaults.colors(
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedContainerColor = Color.White
                    )
                )
                Button(
                    onClick = {
                        isValid = startTime.toIntOrNull() != null && endTime.toIntOrNull() != null
                        if(startTime.isNotEmpty() && endTime.isNotEmpty()){
                            if(isValid){
                                navCtrl.navigate("result/$selectedSport/$startTime/$endTime/${selectedDate.value}")
                            }
                            else{
                                Toast.makeText(
                                    nContext,
                                    "Waktu harus berupa angka!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                        else{
                            Toast.makeText(
                                nContext,
                                "Semua data harus diisi!",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 80.dp)
                        .padding(top = 8.dp)
                        .padding(bottom = 20.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF54BDFE)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Cari", color = Color.Black, fontSize = 15.sp)
                }
            }
        }
    }
}

@Composable
fun DropdownItem(label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Dropdown",
            Modifier.clickable { onClick() }
        )
    }
}
