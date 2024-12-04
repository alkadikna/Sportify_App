package com.example.sportify.ui

import android.app.DatePickerDialog
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sportify.layout_component.BottomNavigationBar
import com.example.sportify.layout_component.TopSection
import com.example.sportify.ui.theme.SportifyTheme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun ScheduleLayout(navCtrl: NavController){
    // Dummy data
    val reservations = mapOf(
        "Lapangan 1" to listOf("9:00" to "Alif"),
        "Lapangan 3" to listOf("11:00" to "Rasyad"),
        "Lapangan 2" to listOf("13:00" to "Marshel")
    )

    // State
    val selectedDate = remember { mutableStateOf("Jumat, 18 Oktober 2024") }
    val calendar = remember { Calendar.getInstance() }
    val context = LocalContext.current

    fun updateDate(action: String) {
        when (action) {
            "previous" -> calendar.add(Calendar.DAY_OF_MONTH, -1) // Kurangi 1 hari
            "next" -> calendar.add(Calendar.DAY_OF_MONTH, 1) // Tambah 1 hari
            "select" -> { /* Pemilihan melalui DatePicker */ }
        }
        
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        selectedDate.value = dateFormat.format(calendar.time)
    }

    // Memperbarui tanggal awal berdasarkan default di state
    LaunchedEffect(Unit) {
        val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("id", "ID"))
        calendar.time = dateFormat.parse(selectedDate.value) ?: Calendar.getInstance().time
    }

    // UI Begin
    Scaffold (
        topBar = { FloatingDropDown() },
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
                    reservations = reservations,
                    selectedDate = selectedDate.value,
                    onDateChange = { action ->
                        if (action == "select") {
                            // Dialog DatePicker
                            DatePickerDialog(
                                context,
                                { _, year, month, dayOfMonth ->
                                    calendar.set(year, month, dayOfMonth)
                                    updateDate("") // Format ulang tanggal setelah dipilih
                                },
                                calendar.get(Calendar.YEAR),
                                calendar.get(Calendar.MONTH),
                                calendar.get(Calendar.DAY_OF_MONTH)
                            ).show()
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
fun FloatingDropDown() {
    Box(modifier = Modifier.fillMaxWidth()) {
        TopSection()
        DropDown(modifier = Modifier)
    }
}

@Composable
fun DropDown(modifier: Modifier) {
    val context = LocalContext.current
    val listSport = arrayOf("Badminton", "Tennis", "Futsal", "Basket")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("Cabang Olahraga") }

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
                text = selectedText,
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
                                selectedText = item
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
    reservations: Map<String, List<Pair<String, String>>>,
    selectedDate: String,
    onDateChange: (String) -> Unit

) {
    val timeSlots = (8..22).map { "$it:00" }

    val columnWeight = .3f

    Column(
        Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            IconButton(onClick = { onDateChange("previous") }) { // Tanggal sebelumnya
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous")
            }
            TextButton(onClick = { onDateChange("select") }) { // Memunculkan DatePicker
                Text(text = selectedDate, style = androidx.compose.material.MaterialTheme.typography.h6)
            }
            IconButton(onClick = { onDateChange("next") }) { // Tanggal berikutnya
                Icon(Icons.Default.ArrowForward, contentDescription = "Next")
            }
        }

        // Header kolom
        Row(
            Modifier
                .background(Color.Gray)
                .fillMaxWidth()) {
            listOf("Lapangan 1", "Lapangan 2", "Lapangan 3").forEach { title ->
                TableCellParent(text = title, weight = columnWeight)
            }
        }

        // Baris waktu dan data pemesanan
        LazyColumn(Modifier.fillMaxSize()) {
            items(timeSlots) { time ->
                Row(Modifier.fillMaxWidth()) {
                    listOf("Lapangan 1", "Lapangan 2", "Lapangan 3").forEach { lapangan ->
                        val reservation = reservations[lapangan]?.find { it.first == time }
                        TableCell(
                            time = time,
                            text = reservation?.second ?: "",
                            weight = columnWeight,
                            backgroundColor = if (reservation != null) Color.Red.copy(alpha = 0.1f) else Color.White
                        )
                    }
                }
            }
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
        Text(text = text, style = androidx.compose.material.MaterialTheme.typography.body2)
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
            Text(text = time, style = androidx.compose.material.MaterialTheme.typography.caption, color = Color.Gray) // Label waktu
            Text(text = text, style = androidx.compose.material.MaterialTheme.typography.caption, color = Color.Gray, fontWeight = FontWeight.Bold)
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