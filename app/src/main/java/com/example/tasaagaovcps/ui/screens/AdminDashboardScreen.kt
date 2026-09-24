package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme

data class RegisteredStudentItem(
    val admNo: String,
    val name: String,
    val className: String,
    val type: String
)

@Composable
fun AdminDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val studentList = remember {
        mutableStateListOf(
            RegisteredStudentItem("TAS-2026-001", "Achola Mary", "P.6A", "Day"),
            RegisteredStudentItem("TAS-2026-002", "Okello James", "P.4A", "Boarding"),
            RegisteredStudentItem("TAS-2026-003", "Nakato Esther", "P.7A", "Day")
        )
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
                            val autoAdmNo = "TAS-2026-${(100..999).random()}"
                            studentList.add(0, RegisteredStudentItem(autoAdmNo, "$fname $lname", className, studentType))
                            showRegisterStudentDialog = false
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
        item {
            AdminHeader(onLogout)
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
            SummaryGrid(studentCount = 247 + studentList.size - 3)
        }

        item {
            RegisteredStudentsCard(students = studentList)
        }

        item {
            FeeCollectionCard()
        }

        item {
            EnrollmentCard()
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
                    text = "Term 3, 2026 • Full Overview",
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
fun SummaryGrid(studentCount: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Students",
                value = studentCount.toString(),
                subText = "Day & Boarding",
                icon = Icons.Rounded.People,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Today's Attendance",
                value = "93%",
                subText = "230 present • 17 absent",
                icon = Icons.Rounded.CheckCircle,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RegisteredStudentsCard(students: List<RegisteredStudentItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("🎓 Registered Students Directory", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            students.forEach { s ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(s.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                        Text("${s.admNo} • ${s.className}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(s.type, style = MaterialTheme.typography.labelSmall, color = Color(0xFF1B5E20), modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun FeeCollectionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📊 Fee Collection by Class", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val classes = listOf(
                "P.7" to 0.90f,
                "P.6" to 0.80f,
                "P.1" to 0.85f,
                "P.4" to 0.72f,
                "P.5" to 0.58f
            )

            classes.forEach { (name, progress) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(name, modifier = Modifier.width(32.dp), style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .padding(horizontal = 8.dp),
                        color = if (progress > 0.7f) Color(0xFF4CAF50) else Color(0xFFBC9522),
                        trackColor = Color(0xFFEEEEEE),
                    )
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun EnrollmentCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("👥 Enrollment Breakdown", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                EnrollmentStat("Boys", "128", Color(0xFF2196F3))
                EnrollmentStat("Girls", "119", Color(0xFFE91E63))
                EnrollmentStat("Day", "162", Color(0xFF7B1FA2))
                EnrollmentStat("Boarding", "85", Color(0xFFFF9800))
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

@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    TasaagaOVCPSTheme {
        AdminDashboardScreen(onLogout = {})
    }
}
