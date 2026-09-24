package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material.icons.automirrored.rounded.ListAlt
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme

data class StudentAttendanceState(
    val id: Int,
    val name: String,
    val admNo: String,
    var status: String,
    var note: String = ""
)

data class StudentResultEntry(
    val studentName: String,
    val maths: Int,
    val english: Int,
    val science: Int,
    val sst: Int,
    val re: Int
) {
    val total: Int get() = maths + english + science + sst + re
    val average: Double get() = total / 5.0
    val grade: String get() = when {
        average >= 80 -> "D1 (Distinction)"
        average >= 70 -> "D2 (Distinction)"
        average >= 60 -> "C3 (Credit)"
        average >= 50 -> "C4 (Credit)"
        else -> "P7 (Pass)"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val studentList = remember {
        mutableStateListOf(
            StudentAttendanceState(1, "Achola Mary", "TAS-2026-001", "Present"),
            StudentAttendanceState(2, "Adong Florence", "TAS-2026-008", "Present"),
            StudentAttendanceState(3, "Akello Susan", "TAS-2026-009", "Absent", "Sick - Parent Called"),
            StudentAttendanceState(4, "Okello James", "TAS-2026-002", "Late", "Arrived 8:30 AM"),
            StudentAttendanceState(5, "Mugisha Peter", "TAS-2026-004", "Present"),
            StudentAttendanceState(6, "Atim Grace", "TAS-2026-005", "Present")
        )
    }

    val resultsList = remember {
        mutableStateListOf(
            StudentResultEntry("Achola Mary", 88, 82, 74, 79, 91),
            StudentResultEntry("Adong Florence", 78, 85, 80, 84, 88),
            StudentResultEntry("Okello James", 65, 72, 68, 70, 75)
        )
    }

    var selectedStudentForEdit by remember { mutableStateOf<StudentAttendanceState?>(null) }
    var showEnterMarksDialog by remember { mutableStateOf(false) }
    var showClassListDialog by remember { mutableStateOf(false) }
    var showTimetableDialog by remember { mutableStateOf(false) }

    val presentCount = studentList.count { it.status == "Present" || it.status == "Late" }
    val absentCount = studentList.size - presentCount

    // 1. ENTER MARKS DIALOG
    if (showEnterMarksDialog) {
        var selectedStudentName by remember { mutableStateOf(studentList[0].name) }
        var mathsText by remember { mutableStateOf("80") }
        var englishText by remember { mutableStateOf("75") }
        var scienceText by remember { mutableStateOf("70") }
        var sstText by remember { mutableStateOf("82") }
        var reText by remember { mutableStateOf("85") }
        var dropdownExpanded by remember { mutableStateOf(false) }

        val m = mathsText.toIntOrNull() ?: 0
        val e = englishText.toIntOrNull() ?: 0
        val sc = scienceText.toIntOrNull() ?: 0
        val ss = sstText.toIntOrNull() ?: 0
        val r = reText.toIntOrNull() ?: 0

        val calcTotal = m + e + sc + ss + r
        val calcAvg = calcTotal / 5.0
        val calcGrade = when {
            calcAvg >= 80 -> "D1 (Distinction)"
            calcAvg >= 70 -> "D2 (Distinction)"
            calcAvg >= 60 -> "C3 (Credit)"
            calcAvg >= 50 -> "C4 (Credit)"
            else -> "P7 (Pass)"
        }

        AlertDialog(
            onDismissRequest = { showEnterMarksDialog = false },
            title = { Text("Enter Exam Marks — P.5A Term 3", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = dropdownExpanded,
                        onExpandedChange = { dropdownExpanded = !dropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedStudentName,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Select Student") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = dropdownExpanded,
                            onDismissRequest = { dropdownExpanded = false }
                        ) {
                            studentList.forEach { student ->
                                DropdownMenuItem(
                                    text = { Text(student.name) },
                                    onClick = {
                                        selectedStudentName = student.name
                                        dropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = mathsText,
                            onValueChange = { mathsText = it },
                            label = { Text("Maths") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = englishText,
                            onValueChange = { englishText = it },
                            label = { Text("English") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = scienceText,
                            onValueChange = { scienceText = it },
                            label = { Text("Science") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = sstText,
                            onValueChange = { sstText = it },
                            label = { Text("SST") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true
                        )
                    }

                    OutlinedTextField(
                        value = reText,
                        onValueChange = { reText = it },
                        label = { Text("RE") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Surface(
                        color = Color(0xFFFFF8E1),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Summary Calculation:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Total Marks: $calcTotal / 500  •  Average: ${"%.1f".format(calcAvg)}%", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Text("Computed Grade: $calcGrade", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val newEntry = StudentResultEntry(selectedStudentName, m, e, sc, ss, r)
                        val idx = resultsList.indexOfFirst { it.studentName == selectedStudentName }
                        if (idx != -1) {
                            resultsList[idx] = newEntry
                        } else {
                            resultsList.add(newEntry)
                        }
                        showEnterMarksDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522))
                ) {
                    Text("Save & Submit Results", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEnterMarksDialog = false }) { Text("Cancel") }
            }
        )
    }

    // 2. CLASS LIST DIALOG
    if (showClassListDialog) {
        AlertDialog(
            onDismissRequest = { showClassListDialog = false },
            title = { Text("P.5A Enrolled Class List", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    studentList.forEach { st ->
                        ListItem(
                            headlineContent = { Text(st.name, fontWeight = FontWeight.Bold) },
                            supportingContent = { Text("Admission ID: ${st.admNo}") },
                            leadingContent = { Icon(Icons.Rounded.Person, contentDescription = null, tint = Color(0xFFBC9522)) },
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showClassListDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522))
                ) {
                    Text("Close")
                }
            }
        )
    }

    // 3. TIMETABLE DIALOG
    if (showTimetableDialog) {
        AlertDialog(
            onDismissRequest = { showTimetableDialog = false },
            title = { Text("P.5A Weekly Class Timetable", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val slots = listOf(
                        "8:00 AM - 9:30 AM" to "Mathematics (P.5A)",
                        "9:30 AM - 10:30 AM" to "English Language (P.5A)",
                        "10:30 AM - 11:00 AM" to "Morning Break / Meal",
                        "11:00 AM - 12:30 PM" to "Integrated Science (P.5A)",
                        "2:00 PM - 3:30 PM" to "Social Studies & RE (P.5A)"
                    )
                    slots.forEach { (time, subject) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(time, style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                            Text(subject, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showTimetableDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522))
                ) {
                    Text("Close")
                }
            }
        )
    }

    // EDIT ATTENDANCE DIALOG
    if (selectedStudentForEdit != null) {
        val s = selectedStudentForEdit!!
        var currentStatus by remember { mutableStateOf(s.status) }
        var currentNote by remember { mutableStateOf(s.note) }

        AlertDialog(
            onDismissRequest = { selectedStudentForEdit = null },
            title = {
                Text("Update Attendance — ${s.name}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Admission No: ${s.admNo}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)

                    Text("Select Status:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)

                    val statuses = listOf("Present", "Absent", "Late", "Excused")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        statuses.forEach { st ->
                            FilterChip(
                                selected = currentStatus == st,
                                onClick = { currentStatus = st },
                                label = { Text(st, fontSize = 11.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = currentNote,
                        onValueChange = { currentNote = it },
                        label = { Text("Note / Permission Reason (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val index = studentList.indexOfFirst { it.id == s.id }
                        if (index != -1) {
                            studentList[index] = s.copy(status = currentStatus, note = currentNote)
                        }
                        selectedStudentForEdit = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522))
                ) {
                    Text("Save Changes", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedStudentForEdit = null }) {
                    Text("Cancel")
                }
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
            TeacherHeader(onLogout)
        }

        item {
            TeacherActionGrid(
                onTakeAttendance = {
                    if (studentList.isNotEmpty()) selectedStudentForEdit = studentList[0]
                },
                onEnterMarks = { showEnterMarksDialog = true },
                onClassList = { showClassListDialog = true },
                onTimetable = { showTimetableDialog = true }
            )
        }

        item {
            TeacherSummaryGrid(total = studentList.size, present = presentCount, absent = absentCount)
        }

        item {
            ClassAttendanceCard(
                students = studentList,
                onStudentClick = { student ->
                    selectedStudentForEdit = student
                }
            )
        }

        item {
            SubmittedResultsCard(results = resultsList)
        }

        item {
            PerformanceSubjectCard()
        }
    }
}

@Composable
fun TeacherHeader(onLogout: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBC9522)) // Brand Gold
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
                Text("👩‍🏫", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Teacher Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Ms. Amoko Sarah • P.5A • Term 3, 2026",
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
fun TeacherActionGrid(
    onTakeAttendance: () -> Unit,
    onEnterMarks: () -> Unit,
    onClassList: () -> Unit,
    onTimetable: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TeacherActionTile("Attendance", Icons.AutoMirrored.Rounded.Assignment, Modifier.weight(1f), onTakeAttendance)
        TeacherActionTile("Enter Marks", Icons.Rounded.Edit, Modifier.weight(1f), onEnterMarks)
        TeacherActionTile("Class List", Icons.AutoMirrored.Rounded.ListAlt, Modifier.weight(1f), onClassList)
        TeacherActionTile("Timetable", Icons.Rounded.CalendarToday, Modifier.weight(1f), onTimetable)
    }
}

@Composable
fun TeacherActionTile(
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFFBC9522), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun TeacherSummaryGrid(total: Int, present: Int, absent: Int) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        SummaryCard(
            title = "P.5A Enrolled",
            value = total.toString(),
            subText = "Enrolled this term",
            icon = Icons.Rounded.Groups,
            color = Color(0xFF2196F3),
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Present Today",
            value = present.toString(),
            subText = "$absent absent",
            icon = Icons.Rounded.HowToReg,
            color = Color(0xFF4CAF50),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ClassAttendanceCard(
    students: List<StudentAttendanceState>,
    onStudentClick: (StudentAttendanceState) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("📋 Today's Attendance — P.5A", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text("Tap student to edit", style = MaterialTheme.typography.labelSmall, color = Color(0xFFBC9522), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            students.forEach { s ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onStudentClick(s) }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(s.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        if (s.note.isNotBlank()) {
                            Text(s.note, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }

                    Surface(
                        color = when (s.status) {
                            "Present" -> Color(0xFFE8F5E9)
                            "Late" -> Color(0xFFFFF8E1)
                            "Excused" -> Color(0xFFE3F2FD)
                            else -> Color(0xFFFFEBEE)
                        },
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = s.status,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = when (s.status) {
                                "Present" -> Color(0xFF1B5E20)
                                "Late" -> Color(0xFFF57F17)
                                "Excused" -> Color(0xFF1565C0)
                                else -> Color(0xFFB71C1C)
                            },
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun SubmittedResultsCard(results: List<StudentResultEntry>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📝 Term 3 Exam Results Entries", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            results.forEach { r ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(r.studentName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                        Text("M:${r.maths} E:${r.english} SC:${r.science} SST:${r.sst} RE:${r.re}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text("${"%.0f".format(r.average)}% Avg", fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20), style = MaterialTheme.typography.titleSmall)
                        Text(r.grade, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun PerformanceSubjectCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📊 P.5A Subject Performance Averages", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val subjects = listOf(
                "Maths" to 0.72f,
                "English" to 0.81f,
                "Science" to 0.68f,
                "SST" to 0.79f,
                "RE" to 0.88f
            )

            subjects.forEach { (name, score) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(name, modifier = Modifier.width(60.dp), style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(
                        progress = { score },
                        modifier = Modifier.weight(1f).height(6.dp).padding(horizontal = 8.dp),
                        color = Color(0xFFBC9522),
                        trackColor = Color(0xFFEEEEEE),
                    )
                    Text("${(score * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TeacherDashboardPreview() {
    TasaagaOVCPSTheme {
        TeacherDashboardScreen(onLogout = {})
    }
}
