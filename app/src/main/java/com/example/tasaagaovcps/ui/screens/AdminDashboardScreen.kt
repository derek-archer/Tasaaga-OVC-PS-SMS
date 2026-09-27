package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.tasaagaovcps.data.model.Student
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.AdminViewModel
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider

@Composable
fun AdminDashboardScreen(
    schoolId: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    adminViewModel: AdminViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by adminViewModel.state.collectAsState()

    LaunchedEffect(schoolId) {
        adminViewModel.load(schoolId)
    }

    var showRegisterStudentDialog by remember { mutableStateOf(false) }

    // REGISTER STUDENT DIALOG
    if (showRegisterStudentDialog) {
        var fname by remember { mutableStateOf("") }
        var lname by remember { mutableStateOf("") }
        var className by remember { mutableStateOf("P.5A") }
        var studentType by remember { mutableStateOf("Day") }
        var guardianName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showRegisterStudentDialog = false },
            title = { Text("Register New Student", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = fname,
                        onValueChange = { fname = it },
                        label = { Text("First Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = lname,
                        onValueChange = { lname = it },
                        label = { Text("Last Name / Surname *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Text("Student Type:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FilterChip(
                            selected = studentType == "Day",
                            onClick = { studentType = "Day" },
                            label = { Text("Day Student") }
                        )
                        FilterChip(
                            selected = studentType == "Boarding",
                            onClick = { studentType = "Boarding" },
                            label = { Text("Boarding Student") }
                        )
                    }
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Class Stream (e.g. P.5A)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = guardianName,
                        onValueChange = { guardianName = it },
                        label = { Text("Parent / Guardian Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (fname.isNotBlank() && lname.isNotBlank()) {
                            val autoAdmNo = "TAS-${java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)}-${(100..999).random()}"
                            adminViewModel.addStudent(
                                Student(
                                    admNo = autoAdmNo,
                                    fname = fname,
                                    lname = lname,
                                    gender = "Unknown",
                                    studentType = studentType,
                                    guardianName = guardianName,
                                    schoolId = schoolId
                                ),
                                onDone = { showRegisterStudentDialog = false }
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Complete Registration", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRegisterStudentDialog = false }) { Text("Cancel") }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { AdminHeader(onLogout) }

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
                        Text("Student & User Administration", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                        Text("Register new OVC students & manage user roles", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    }
                    Button(
                        onClick = { showRegisterStudentDialog = true },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Rounded.PersonAdd, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add Student", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        item {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                AdminSummaryGrid(
                    totalStudents = state.totalStudents,
                    attendanceRate = state.attendanceRate,
                    presentCount = state.presentCount,
                    absentCount = state.absentCount
                )
            }
        }

        item {
            if (!state.isLoading) {
                LiveStudentsCard(students = state.students.take(20))
            }
        }

        item {
            if (!state.isLoading) {
                EnrollmentBreakdownCard(
                    boysCount = state.boysCount,
                    girlsCount = state.girlsCount,
                    dayCount = state.dayCount,
                    boardingCount = state.boardingCount
                )
            }
        }

        if (state.announcements.isNotEmpty()) {
            item {
                AnnouncementsListCard(
                    title = "📢 School Announcements",
                    announcements = state.announcements
                )
            }
        }
    }
}

@Composable
fun AdminHeader(onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
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
                Text("🏫", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Administration Dashboard",
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
                Icon(Icons.Rounded.Logout, contentDescription = "Logout", tint = Color.White)
            }
        }
    }
}

@Composable
fun AdminSummaryGrid(
    totalStudents: Int,
    attendanceRate: Double,
    presentCount: Int,
    absentCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Students",
                value = totalStudents.toString(),
                subText = "Day & Boarding",
                icon = Icons.Rounded.People,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Today's Attendance",
                value = "${"%.0f".format(attendanceRate)}%",
                subText = "$presentCount present • $absentCount absent",
                icon = Icons.Rounded.CheckCircle,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LiveStudentsCard(students: List<Student>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎓 Registered Students Directory", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            if (students.isEmpty()) {
                Text("No students found.", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
            } else {
                Spacer(modifier = Modifier.height(12.dp))
                students.forEach { s ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(s.displayName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                            Text("${s.admNo ?: "—"} • ${s.classLabel}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                        val isBoarding = s.studentType == "Boarding"
                        Surface(
                            color = if (isBoarding) Color(0xFFE8EAF6) else Color(0xFFE8F5E9),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                s.studentType ?: "Day",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isBoarding) Color(0xFF1A237E) else Color(0xFF1B5E20),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                }
            }
        }
    }
}

@Composable
fun EnrollmentBreakdownCard(boysCount: Int, girlsCount: Int, dayCount: Int, boardingCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👥 Enrollment Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                EnrollmentStat("Boys", boysCount.toString(), Color(0xFF2196F3))
                EnrollmentStat("Girls", girlsCount.toString(), Color(0xFFE91E63))
                EnrollmentStat("Day", dayCount.toString(), Color(0xFF7B1FA2))
                EnrollmentStat("Boarding", boardingCount.toString(), Color(0xFFFF9800))
            }
        }
    }
}

@Composable
fun EnrollmentStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
    }
}

@Composable
fun AnnouncementsListCard(title: String, announcements: List<com.example.tasaagaovcps.data.model.AnnouncementItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            announcements.take(5).forEach { ann ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF8)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(ann.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = Color(0xFF1A3A6B))
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(ann.body, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    TasaagaOVCPSTheme {
        // Preview uses an empty schoolId; real usage passes from login
        AdminDashboardScreen(schoolId = "preview", onLogout = {})
    }
}
