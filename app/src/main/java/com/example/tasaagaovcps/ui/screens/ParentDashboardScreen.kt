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
fun ParentDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showReportCardDialog by remember { mutableStateOf(false) }

    // REPORT CARD MODAL DIALOG
    if (showReportCardDialog) {
        AlertDialog(
            onDismissRequest = { showReportCardDialog = false },
            title = {
                Column {
                    Text("Official Term Report Card", fontWeight = FontWeight.Bold)
                    Text("Mary Achola • P.6A • Term 3, 2026", fontSize = 12.sp, color = Color.Gray)
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val fullGrades = listOf(
                        Triple("Mathematics", "88%", "D1 (Distinction)"),
                        Triple("English Language", "82%", "D1 (Distinction)"),
                        Triple("Integrated Science", "74%", "C3 (Credit)"),
                        Triple("Social Studies", "79%", "D2 (Distinction)"),
                        Triple("Religious Education", "91%", "D1 (Distinction)")
                    )

                    fullGrades.forEach { (subject, score, grade) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(subject, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                Text("Grade: $grade", style = MaterialTheme.typography.labelSmall, color = Color(0xFF1B5E20))
                            }
                            Text(score, fontWeight = FontWeight.Black, style = MaterialTheme.typography.titleMedium, color = Color(0xFFBC9522))
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    }

                    Surface(
                        color = Color(0xFFFFF8E1),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Summary & Class Position:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Total Score: 414 / 500  •  Average: 82.8%", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text("Class Rank: 3rd out of 42 students", fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20), fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Class Teacher Remarks: Excellent academic effort and leadership in class.", fontSize = 11.sp, color = Color.DarkGray)
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showReportCardDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522))
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
            ParentHeader(onLogout)
        }

        item {
            ParentSummaryGrid()
        }

        item {
            TermResultsCard(onViewReportCard = { showReportCardDialog = true })
        }

        item {
            SchoolAnnouncementCard()
        }
    }
}

@Composable
fun ParentHeader(onLogout: () -> Unit) {
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
                Text("👨‍👧", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Parent Portal",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "George Achola • Mary Achola (P.6A)",
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
fun ParentSummaryGrid() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Attendance",
                value = "96%",
                subText = "Excellent this term",
                icon = Icons.Rounded.Verified,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Class Position",
                value = "3rd",
                subText = "Of 42 students",
                icon = Icons.Rounded.BarChart,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Fee Balance",
                value = "UGX 0",
                subText = "Fully paid ✓",
                icon = Icons.Rounded.CreditCard,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Announcements",
                value = "2",
                subText = "From school",
                icon = Icons.Rounded.Campaign,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun TermResultsCard(onViewReportCard: () -> Unit) {
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
                Text("📊 Term 3 Results — Mary Achola", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = onViewReportCard,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Report Card", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            val grades = listOf(
                Triple("Maths", "88", "A"),
                Triple("English", "82", "A"),
                Triple("Science", "74", "B"),
                Triple("SST", "79", "B+"),
                Triple("RE", "91", "A+")
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                grades.forEach { (subject, score, grade) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(subject, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(score, fontWeight = FontWeight.Black, fontSize = 16.sp, color = if (score.toInt() > 80) Color(0xFF4CAF50) else Color(0xFFBC9522))
                        Text(grade, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SchoolAnnouncementCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📢 School Announcement", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF8)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "End of term exams begin Monday 28th October. All fees must be cleared before exam week.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1A3A6B)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Headteacher • 2 days ago", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParentDashboardPreview() {
    TasaagaOVCPSTheme {
        ParentDashboardScreen(onLogout = {})
    }
}
