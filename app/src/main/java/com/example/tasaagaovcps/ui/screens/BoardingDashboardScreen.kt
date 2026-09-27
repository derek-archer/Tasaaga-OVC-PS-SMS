package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasaagaovcps.data.model.BoardingDorm
import com.example.tasaagaovcps.data.model.WelfareIncident
import com.example.tasaagaovcps.ui.viewmodel.BoardingViewModel
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider
import com.example.tasaagaovcps.ui.viewmodel.BoardingViewModel

@Composable
fun BoardingDashboardScreen(
    schoolId: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    boardingViewModel: BoardingViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by boardingViewModel.state.collectAsState()

    LaunchedEffect(schoolId) {
        boardingViewModel.load(schoolId)
    }

    var showAllocateBedDialog by remember { mutableStateOf(false) }
    var showLogIncidentDialog by remember { mutableStateOf(false) }

    // ALLOCATE BED DIALOG
    if (showAllocateBedDialog) {
        var studentName by remember { mutableStateOf("") }
        var selectedDorm by remember { mutableStateOf("") }
        var roomBed by remember { mutableStateOf("") }

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
                        label = { Text("Dormitory Name") },
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
                    onClick = { if (studentName.isNotBlank()) showAllocateBedDialog = false },
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
        var incidentType by remember { mutableStateOf("Health") }
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
                    Text("Incident Type:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Health", "Discipline", "General").forEach { type ->
                            FilterChip(
                                selected = incidentType == type,
                                onClick = { incidentType = type },
                                label = { Text(type, fontSize = 11.sp) }
                            )
                        }
                    }
                    OutlinedTextField(
                        value = incidentDetails,
                        onValueChange = { incidentDetails = it },
                        label = { Text("Incident Description *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (studentName.isNotBlank() && incidentDetails.isNotBlank()) {
                            boardingViewModel.reportIncident(
                                studentName = studentName,
                                incidentType = incidentType,
                                description = incidentDetails
                            )
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
        item { BoardingHeader(onLogout) }

        if (state.error != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        state.error!!,
                        modifier = Modifier.padding(12.dp),
                        color = Color(0xFFB71C1C),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
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
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val capacityPercent = if (state.totalCapacity > 0)
                    (state.totalOccupied.toDouble() / state.totalCapacity * 100).toInt() else 0
                val availableBeds = state.totalCapacity - state.totalOccupied

                BoardingSummaryGrid(
                    totalBoarders = state.totalBoarders,
                    boysCount = state.boysCount,
                    girlsCount = state.girlsCount,
                    dormCount = state.dorms.size,
                    capacityPercent = capacityPercent,
                    availableBeds = availableBeds,
                    welfareCount = state.welfareIncidents.count { it.status == "Active" }
                )
            }
        }

        if (!state.isLoading && state.dorms.isNotEmpty()) {
            item {
                LiveDormitoryCard(dorms = state.dorms)
            }
        }

        if (!state.isLoading && state.welfareIncidents.isNotEmpty()) {
            item {
                LiveWelfareFlagsCard(incidents = state.welfareIncidents)
            }
        }
    }
}

@Composable
fun BoardingHeader(onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A3A6B))
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
                    text = "Tasaaga OVC Primary School",
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
fun BoardingSummaryGrid(
    totalBoarders: Int,
    boysCount: Int,
    girlsCount: Int,
    dormCount: Int,
    capacityPercent: Int,
    availableBeds: Int,
    welfareCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Boarders",
                value = totalBoarders.toString(),
                subText = "Boys: $boysCount • Girls: $girlsCount",
                icon = Icons.Rounded.Bed,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Capacity Used",
                value = "$capacityPercent%",
                subText = "$availableBeds beds available",
                icon = Icons.Rounded.PieChart,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Dormitories",
                value = dormCount.toString(),
                subText = "Active dorms",
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
fun LiveDormitoryCard(dorms: List<BoardingDorm>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🏠 Dormitory Occupancy", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            dorms.forEach { dorm ->
                val occupancy = if (dorm.capacity > 0) dorm.occupied.toFloat() / dorm.capacity.toFloat() else 0f
                val isBoysFlag = dorm.gender?.lowercase()?.contains("boy") == true || dorm.gender?.lowercase() == "male"

                Column(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            dorm.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "${dorm.occupied}/${dorm.capacity} (${(occupancy * 100).toInt()}%)",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    LinearProgressIndicator(
                        progress = { occupancy },
                        modifier = Modifier.fillMaxWidth().height(8.dp).padding(top = 4.dp),
                        color = if (isBoysFlag) Color(0xFF1A3A6B) else Color(0xFFEE5A5A),
                        trackColor = Color(0xFFEEEEEE),
                    )
                }
            }
        }
    }
}

@Composable
fun LiveWelfareFlagsCard(incidents: List<WelfareIncident>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("⚠️ Welfare Flags & Incidents", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            incidents.take(10).forEach { item ->
                ListItem(
                    headlineContent = { Text(item.studentName, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text("[${item.incidentType}] ${item.description}") },
                    leadingContent = {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color(0xFFF44336), RoundedCornerShape(4.dp))
                        )
                    },
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
        BoardingDashboardScreen(schoolId = "preview", onLogout = {})
    }
}
