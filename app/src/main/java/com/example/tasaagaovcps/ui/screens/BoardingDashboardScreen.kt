package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme

data class WelfareIncidentItem(
    val id: Int,
    val studentName: String,
    val issue: String,
    var status: String = "Active"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardingDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val incidentsList = remember {
        mutableStateListOf(
            WelfareIncidentItem(1, "Akello Susan (P.5A)", "Reported sick • Dorm C, Room 2"),
            WelfareIncidentItem(2, "Onen David (P.7B)", "Parent collecting today")
        )
    }

    var showAllocateBedDialog by remember { mutableStateOf(false) }
    var showLogIncidentDialog by remember { mutableStateOf(false) }

    // ALLOCATE BED DIALOG
    if (showAllocateBedDialog) {
        var studentName by remember { mutableStateOf("") }
        var selectedDorm by remember { mutableStateOf("Dorm A — Boys") }
        var roomBed by remember { mutableStateOf("Room 2, Bed 04") }

        AlertDialog(
            onDismissRequest = { showAllocateBedDialog = false },
            title = { Text("Allocate Dormitory Bed", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Boarding Student Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = selectedDorm,
                        onValueChange = { selectedDorm = it },
                        label = { Text("Dormitory (Dorm A/B/C/D)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = roomBed,
                        onValueChange = { roomBed = it },
                        label = { Text("Room & Bed Number") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (studentName.isNotBlank()) showAllocateBedDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B))
                ) {
                    Text("Confirm Bed Allocation")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAllocateBedDialog = false }) { Text("Cancel") }
            }
        )
    }

    // LOG INCIDENT DIALOG
    if (showLogIncidentDialog) {
        var studentName by remember { mutableStateOf("") }
        var incidentDetails by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showLogIncidentDialog = false },
            title = { Text("Log Welfare / Health Incident", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name & Class *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = incidentDetails,
                        onValueChange = { incidentDetails = it },
                        label = { Text("Incident / Health Note *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (studentName.isNotBlank() && incidentDetails.isNotBlank()) {
                            incidentsList.add(0, WelfareIncidentItem(incidentsList.size + 1, studentName, incidentDetails))
                            showLogIncidentDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Log Incident")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogIncidentDialog = false }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            BoardingHeader(onLogout)
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Dormitory & Welfare Operations", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Manage bed allocations & welfare logs", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = { showAllocateBedDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Allocate Bed", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = { showLogIncidentDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Log Issue", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            BoardingSummaryGrid(welfareCount = incidentsList.count { it.status == "Active" })
        }

        item {
            DormitoryOccupancyCard()
        }

        item {
            WelfareFlagsCard(incidents = incidentsList)
        }
    }
}

@Composable
fun BoardingHeader(onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A6B)) // Website Blue
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text("🏘️", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Boarding Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Ms. Akello Ruth • Boarding Officer",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            IconButton(onClick = onLogout) {
                Icon(Icons.AutoMirrored.Rounded.Logout, contentDescription = "Logout", tint = Color.White)
            }
        }
    }
}

@Composable
fun BoardingSummaryGrid(welfareCount: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Boarders",
                value = "85",
                subText = "Boys: 48 • Girls: 37",
                icon = Icons.Rounded.Bed,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Capacity Used",
                value = "85%",
                subText = "15 beds available",
                icon = Icons.Rounded.PieChart,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Dormitories",
                value = "4",
                subText = "2 boys • 2 girls",
                icon = Icons.Rounded.Domain,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Welfare Issues",
                value = welfareCount.toString(),
                subText = "Needs attention",
                icon = Icons.Rounded.MedicalServices,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DormitoryOccupancyCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🏠 Dormitory Occupancy", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val dorms = listOf(
                "Dorm A — Boys" to 22f/25f,
                "Dorm B — Boys" to 26f/30f,
                "Dorm C — Girls" to 20f/25f,
                "Dorm D — Girls" to 17f/20f
            )

            dorms.forEach { (name, occupancy) ->
                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        Text("${(occupancy * 100).toInt()}%", style = MaterialTheme.typography.labelSmall)
                    }
                    LinearProgressIndicator(
                        progress = { occupancy },
                        modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 4.dp),
                        color = if (name.contains("Boys")) Color(0xFF1A3A6B) else Color(0xFFEE5A5A),
                        trackColor = Color(0xFFEEEEEE),
                    )
                }
            }
        }
    }
}

@Composable
fun WelfareFlagsCard(incidents: List<WelfareIncidentItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("⚠️ Welfare Flags & Incidents", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            incidents.forEach { item ->
                ListItem(
                    headlineContent = { Text(item.studentName, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(item.issue) },
                    leadingContent = { Box(modifier = Modifier.size(8.dp).background(Color(0xFFF44336), RoundedCornerShape(4.dp))) },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BoardingDashboardPreview() {
    TasaagaOVCPSTheme {
        BoardingDashboardScreen(onLogout = {})
    }
}
