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
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider
import com.example.tasaagaovcps.ui.viewmodel.ParentViewModel

@Composable
fun ParentDashboardScreen(
    schoolId: String,
    studentId: Int? = null,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    parentViewModel: ParentViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by parentViewModel.state.collectAsState()

    LaunchedEffect(schoolId, studentId) {
        parentViewModel.load(schoolId, studentId)
    }

    var showReportCardDialog by remember { mutableStateOf(false) }

    // REPORT CARD MODAL DIALOG
    if (showReportCardDialog && state.examResults.isNotEmpty()) {
        val results = state.examResults
        val totalMarks = results.sumOf { it.marks }
        val maxTotal = results.sumOf { it.maxMarks }
        val avg = if (results.isNotEmpty()) totalMarks.toDouble() / results.size else 0.0

        AlertDialog(
            onDismissRequest = { showReportCardDialog = false },
            title = {
                Column {
                    Text("Official Term Report Card", fontWeight = FontWeight.Bold)
                    state.student?.let { s ->
                        Text("${s.fname} ${s.lname} • Class ${s.classId ?: "—"}", fontSize = 12.sp, color = Color.Gray)
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    results.forEach { r ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(r.subject, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                                Text("Grade: ${r.grade ?: "—"}", style = MaterialTheme.typography.labelSmall, color = Color(0xFF1B5E20))
                            }
                            Text(
                                "${r.marks}/${r.maxMarks}",
                                fontWeight = FontWeight.Black,
                                style = MaterialTheme.typography.titleMedium,
                                color = Color(0xFFBC9522)
                            )
                        }
                        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    }

                    Surface(
                        color = Color(0xFFFFF8E1),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Summary:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("Total: $totalMarks / $maxTotal  •  Average: ${"%.1f".format(avg)}%", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            state.classPosition?.let { pos ->
                                Text(
                                    "Class Rank: ${pos}${ordinalSuffix(pos)} of ${state.classTotalStudents}",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20),
                                    fontSize = 12.sp
                                )
                            }
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
        item { ParentHeader(onLogout, state.student?.displayName) }

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
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(120.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                ParentSummaryGrid(
                    attendanceRate = state.attendanceRate,
                    classPosition = state.classPosition,
                    classTotalStudents = state.classTotalStudents,
                    totalFeesPaid = state.totalFeesPaid,
                    announcementsCount = state.announcements.size
                )
            }
        }

        if (!state.isLoading && state.examResults.isNotEmpty()) {
            item {
                LiveTermResultsCard(
                    results = state.examResults,
                    studentName = state.student?.let { "${it.fname} ${it.lname}" } ?: "Student",
                    onViewReportCard = { showReportCardDialog = true }
                )
            }
        }

        if (!state.isLoading && state.payments.isNotEmpty()) {
            item {
                FeePaymentCard(
                    payments = state.payments,
                    totalPaid = state.totalFeesPaid
                )
            }
        }

        if (!state.isLoading && state.announcements.isNotEmpty()) {
            item {
                AnnouncementsListCard(
                    title = "📢 School Announcements",
                    announcements = state.announcements
                )
            }
        }
    }
}

fun ordinalSuffix(n: Int): String = when {
    n in 11..13 -> "th"
    n % 10 == 1 -> "st"
    n % 10 == 2 -> "nd"
    n % 10 == 3 -> "rd"
    else -> "th"
}

@Composable
fun ParentHeader(onLogout: () -> Unit, childName: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFBC9522))
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
                    text = childName?.let { "Child: $it" } ?: "Tasaaga OVC Primary School",
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
fun ParentSummaryGrid(
    attendanceRate: Double,
    classPosition: Int?,
    classTotalStudents: Int,
    totalFeesPaid: Double,
    announcementsCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Attendance",
                value = "${"%.0f".format(attendanceRate)}%",
                subText = "This term",
                icon = Icons.Rounded.Verified,
                color = if (attendanceRate >= 90) Color(0xFF4CAF50) else Color(0xFFFF9800),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Class Position",
                value = classPosition?.let { "$it${ordinalSuffix(it)}" } ?: "—",
                subText = if (classTotalStudents > 0) "Of $classTotalStudents students" else "Not ranked yet",
                icon = Icons.Rounded.BarChart,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Fees Paid",
                value = "UGX %,.0f".format(totalFeesPaid),
                subText = "This term",
                icon = Icons.Rounded.CreditCard,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Announcements",
                value = announcementsCount.toString(),
                subText = "From school",
                icon = Icons.Rounded.Campaign,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun LiveTermResultsCard(
    results: List<com.example.tasaagaovcps.data.model.ExamResult>,
    studentName: String,
    onViewReportCard: () -> Unit
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
                Text(
                    "📊 Results — $studentName",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onViewReportCard,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522)),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text("Report Card", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                results.take(5).forEach { r ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(r.subject.take(4), style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        Text(
                            r.marks.toString(),
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = if (r.marks >= 80) Color(0xFF4CAF50) else Color(0xFFBC9522)
                        )
                        Text(r.grade ?: "—", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun FeePaymentCard(
    payments: List<com.example.tasaagaovcps.data.model.PaymentRecord>,
    totalPaid: Double
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
                Text("💳 Fee Payment History", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                Text(
                    "Total: UGX %,.0f".format(totalPaid),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            payments.take(5).forEach { payment ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(payment.receiptNo, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text("${payment.paymentDate} • ${payment.paymentMethod}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Text(
                        "UGX %,.0f".format(payment.amount),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ParentDashboardPreview() {
    TasaagaOVCPSTheme {
        ParentDashboardScreen(schoolId = "preview", onLogout = {})
    }
}
