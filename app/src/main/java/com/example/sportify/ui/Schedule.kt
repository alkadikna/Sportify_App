package com.example.sportify.ui

import android.app.DatePickerDialog
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.R
import com.example.sportify.Repository.FetchScheduleData
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.TopSection
import com.example.sportify.ui.theme.SportifyTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun ScheduleLayout(navCtrl: NavController) {
    val calendar = remember { Calendar.getInstance() }
    val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale("id", "ID"))

    val selectedDate = remember { mutableStateOf(dateFormat.format(calendar.time)) }
    val formattedDate = remember {
        mutableStateOf(outputFormat.format(calendar.time))
    }
    val showDatePicker = remember { mutableStateOf(false) }
    val selectedFieldType = remember { mutableStateOf("Badminton") }


    // Fungsi untuk memperbarui tanggal
    fun updateDate(action: String) {
        when (action) {
            "previous" -> calendar.add(Calendar.DAY_OF_MONTH, -1) // Kurangi 1 hari
            "next" -> calendar.add(Calendar.DAY_OF_MONTH, 1) // Tambah 1 hari
        }
        selectedDate.value = dateFormat.format(calendar.time)
        formattedDate.value = outputFormat.format(calendar.time)
        Log.d("Tanggal", "Tanggal data berubah: $selectedDate atau $formattedDate")

    }

    Scaffold(
        topBar = { FloatingDropDown(selectedFieldType) },
        bottomBar = {
            BottomNavigationBar(
                navController = navCtrl,
                index = 1
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                TableScreen(
                    fieldType = selectedFieldType.value,
                    selectedDate = formattedDate.value,
                    onDateChange = { action ->
                        if (action == "select") {
                            // Tampilkan DatePicker
                            showDatePicker.value = true
                        } else {
                            updateDate(action) // Previous atau Next
                        }
                    }
                )
            }
        }
    )
}

@Composable
fun FloatingDropDown(selectedFieldType: MutableState<String>) {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopSection()
        DropDown(modifier = Modifier, selectedFieldType = selectedFieldType)
    }
}

@Composable
fun DropDown(modifier: Modifier, selectedFieldType: MutableState<String>) {
    val context = LocalContext.current
    val listSport = arrayOf("Badminton", "Tennis", "Futsal", "Basket")
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(top = 110.dp)
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .shadow(
                    4.dp,
                    shape = RoundedCornerShape(24.dp),
                    ambientColor = Color.Black.copy(0.2f),
                    spotColor = Color.Black.copy(0.2f)
                )
                .background(Color.White, RoundedCornerShape(24.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = selectedFieldType.value,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            Icon(
                imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        AnimatedVisibility(visible = expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Black.copy(0.2f),
                        spotColor = Color.Black.copy(0.2f)
                    )
                    .background(Color.White, shape = RoundedCornerShape(16.dp))
                    .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            ) {
                listSport.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedFieldType.value = item
                                expanded = false
                                Toast
                                    .makeText(context, item, Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun TableScreen(
    selectedDate: String,
    fieldType: String,
    onDateChange: (String) -> Unit
) {
    val timeSlots = (8..22).map { "$it:00" }
    val columnWeight = .3f
    val showDatePicker = remember { mutableStateOf(false) }
    val calendar = Calendar.getInstance()
    val reservations = remember { mutableStateOf<Map<String, Map<String, String>>>(emptyMap()) }
    val isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(selectedDate, fieldType) {
        isLoading.value = true
        kotlinx.coroutines.delay(1000)
        FetchScheduleData(selectedDate, fieldType) { data ->
            isLoading.value = false
            Log.d("FetchScheduleData", "Data: $data")
            val formattedData = mutableMapOf<String, MutableMap<String, String>>()
            data.forEach { time ->
                val fields = mutableMapOf<String, String>()
                time.fieldList.forEach { field ->
                    if (!field.isAvailable) {
                        fields[field.name] = field.user
                    } else {
                        fields[field.name] = ""
                    }
                }
                formattedData["${time.startTime}:00"] = fields
            }
            reservations.value = formattedData
            Log.d("ReservationsData", "Data reservasi: ${reservations.value}")
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Header tanggal
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { onDateChange("previous") }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }
            TextButton(onClick = { showDatePicker.value = true }) {
                Text(text = "Jadwal: $selectedDate", style = MaterialTheme.typography.h6)
            }
            IconButton(onClick = { onDateChange("next") }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        if (isLoading.value) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = colorResource(id = R.color.main_blue))
            }
        } else {
            // Header kolom
            Row(
                Modifier
                    .background(Color.Gray)
                    .fillMaxWidth()
            ) {
                listOf(1, 2, 3).forEach { i ->
                    TableCellParent(text = "$fieldType $i", weight = columnWeight)
                }
            }

            // Data
            LazyColumn(Modifier.fillMaxSize()) {
                items(timeSlots) { time ->
                    Row(Modifier.fillMaxWidth()) {
                        listOf(1, 2, 3).forEach { i ->
                            val lapanganName = "lapangan $fieldType $i"
                            val reservation = reservations.value[time]?.get(lapanganName)
                            val isReserved = reservation != null && reservation != ""
                            TableCell(
                                time = time,
                                text = reservation ?: "",
                                weight = columnWeight,
                                backgroundColor = if (isReserved) Color.Red.copy(alpha = 0.1f) else Color.White
                            )
                        }
                    }
                }
            }
        }

        if (showDatePicker.value) {
            ShowDatePicker(calendar, { year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                showDatePicker.value = false
                onDateChange("select")
            }, showDatePicker)
        }
    }
}


@Composable
fun RowScope.TableCellParent(
    text: String,
    weight: Float,
    backgroundColor: Color = Color(0xFF5AB5FF)
) {
    Box(
        Modifier
            .border(0.1.dp, Color.DarkGray)
            .weight(weight)
            .background(backgroundColor)
            .padding(8.dp)
    ) {
        Text(text = text, style = MaterialTheme.typography.body2, textAlign = TextAlign.Center)
    }
}

@Composable
fun RowScope.TableCell(
    time: String,
    text: String,
    weight: Float,
    backgroundColor: Color = Color.White
) {
    Box(
        Modifier
            .border(0.1.dp, Color.DarkGray)
            .weight(weight)
            .background(backgroundColor)
            .padding(vertical = 20.dp, horizontal = 10.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            Text(text = time, style = MaterialTheme.typography.caption, color = Color.Gray)
            Text(text = text, style = MaterialTheme.typography.caption, color = Color.Gray, fontWeight = FontWeight.Bold)
        }

    }
}

@Composable
fun ShowDatePicker(
    calendar: Calendar,
    onDateSelected: (Int, Int, Int) -> Unit,
    showDatePicker: MutableState<Boolean>
) {
    val context = LocalContext.current

    if (showDatePicker.value) {
        val datePickerDialog = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                onDateSelected(year, month, dayOfMonth)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.setOnCancelListener {
            showDatePicker.value = false
        }

        LaunchedEffect(datePickerDialog) {
            datePickerDialog.show()
        }
    }
}



@Preview(showBackground = true)
@Composable
fun ScheduleLayoutPreview(){
    SportifyTheme {
        ScheduleLayout(navCtrl = rememberNavController())
    }
}