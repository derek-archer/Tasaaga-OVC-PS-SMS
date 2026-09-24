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
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme

@Composable
fun StudentDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showReportCardDialog by remember { mutableStateOf(false) }

    // REPORT CARD MODAL DIALOG FOR STUDENT
    if (showReportCardDialog) {
        AlertDialog(
            onDismissRequest = { showReportCardDialog = false },
            title = {
                Column {
                    Text("My Academic Report Card", fontWeight = FontWeight.Bold)
                    Text("Mukasa David • P.6A • TAS-2026-048", fontSize = 12.sp, color = Color.Gray)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val studentGrades = listOf(
                        Triple("Mathematics", "74%", "C3 (Credit)"),
                        Triple("English Language", "80%", "D1 (Distinction)"),
                        Triple("Integrated Science", "71%", "C3 (Credit)"),
                        Triple("Social Studies", "77%", "D2 (Distinction)"),
                        Triple("Religious Education", "85%", "D1 (Distinction)")
                    )

                    studentGrades.forEach { (subject, score, grade) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(subject, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                Text("Grade: $grade", style = MaterialTheme.typography.labelSmall, color = Color(0xFF1B5E20))
                            }
                            Text(score, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, color = Color(0xFF1A3A6B))
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    }

                    Surface(
                        color = Color(0xFFE3F2FD),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Summary & Class Position:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Total Score: 387 / 500  •  Average: 77.4%", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text("Class Rank: 8th out of 42 students", fontWeight = FontWeight.Bold, color = Color(0xFF1A3A6B), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Teacher Remark: Good performance. Keep up the strong effort in Science!", fontSize = 11.sp, color = Color.DarkGray)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showReportCardDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B))
                ) {
                    Text("Close Report Card")
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
            StudentHeader(onLogout)
        }

        item {
            StudentSummaryGrid()
        }

        item {
            MyResultsCard(onViewReportCard = { showReportCardDialog = true })
        }

        item {
            SchoolAnnouncementsCard()
        }
    }
}

@Composable
fun StudentHeader(onLogout: () -> Unit) {
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
                Text("👦", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Student Portal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Mukasa David • P.6A • TAS-2026-048",
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
fun StudentSummaryGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "My Attendance",
                value = "94%",
                subText = "This term",
                icon = Icons.Rounded.CalendarMonth,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "My Position",
                value = "8th",
                subText = "In P.6A of 42",
                icon = Icons.Rounded.WorkspacePremium,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Subjects",
                value = "6",
                subText = "This term",
                icon = Icons.Rounded.Book,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Announcements",
                value = "3",
                subText = "Unread",
                icon = Icons.Rounded.NotificationsActive,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MyResultsCard(onViewReportCard: () -> Unit) {
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
                Text("📊 My Term 3 Results", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = onViewReportCard,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Report Card", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            val results = listOf(
                Triple("Maths", "74", "B"),
                Triple("English", "80", "A"),
                Triple("Science", "71", "B"),
                Triple("SST", "77", "B+"),
                Triple("RE", "85", "A")
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                results.forEach { (subject, score, grade) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(subject, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(score, fontWeight = FontWeight.Black, fontSize = 16.sp, color = if (score.toInt() >= 80) Color(0xFF4CAF50) else Color(0xFFBC9522))
                        Text(grade, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SchoolAnnouncementsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📢 School Announcements", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            AnnouncementItem("📚 Exam Timetable", "End of term exams begin Monday 28 October.", Color(0xFF1A3A6B))
            Spacer(modifier = Modifier.height(8.dp))
            AnnouncementItem("🏆 Drama Club", "Drama club rehearsals every Wednesday 4PM.", Color(0xFF4CAF50))
        }
    }
}

@Composable
fun AnnouncementItem(title: String, body: String, color: Color) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = color)
            Text(body, style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StudentDashboardPreview() {
    TasaagaOVCPSTheme {
        StudentDashboardScreen(onLogout = {})
    }
}
