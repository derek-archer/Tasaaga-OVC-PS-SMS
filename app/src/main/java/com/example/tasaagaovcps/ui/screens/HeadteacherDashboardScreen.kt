package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.rounded.Grading
import androidx.compose.material.icons.automirrored.rounded.Logout
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
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme

data class PendingApprovalItem(
    val id: Int,
    val title: String,
    val description: String,
    var status: String = "Pending"
)

@Composable
fun HeadteacherDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendingList = remember {
        mutableStateListOf(
            PendingApprovalItem(1, "P.7A Term 3 Results", "Teacher Opio, awaiting academic sign-off"),
            PendingApprovalItem(2, "Expense Requisition", "UGX 450,000 for P.5 & P.6 exercise books"),
            PendingApprovalItem(3, "OVC Fee Support Request", "Okello James (P.4B), subsidized fees")
        )
    }

    var selectedItemForApproval by remember { mutableStateOf<PendingApprovalItem?>(null) }

    if (selectedItemForApproval != null) {
        val item = selectedItemForApproval!!
        AlertDialog(
            onDismissRequest = { selectedItemForApproval = null },
            title = { Text("Review & Approve — ${item.title}", fontWeight = FontWeight.Bold) },
            text = { Text("Details: ${item.description}\n\nDo you want to formally approve this submission?") },
            confirmButton = {
                Button(
                    onClick = {
                        val idx = pendingList.indexOfFirst { it.id == item.id }
                        if (idx != -1) {
                            pendingList[idx] = item.copy(status = "Approved ✓")
                        }
                        selectedItemForApproval = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B5E20))
                ) {
                    Text("Approve Submission")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        val idx = pendingList.indexOfFirst { it.id == item.id }
                        if (idx != -1) {
                            pendingList[idx] = item.copy(status = "Rejected")
                        }
                        selectedItemForApproval = null
                    }
                ) {
                    Text("Reject", color = Color(0xFFB71C1C))
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
            HeadteacherHeader(onLogout)
        }

        item {
            HeadteacherSummaryGrid(pendingCount = pendingList.count { it.status == "Pending" })
        }

        item {
            WeeklyAttendanceTrendCard()
        }

        item {
            PendingApprovalsCard(
                approvals = pendingList,
                onReviewClick = { selectedItemForApproval = it }
            )
        }
    }
}

@Composable
fun HeadteacherHeader(onLogout: () -> Unit) {
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
                Text("👔", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Headteacher Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Mrs. Nakato Rose • Term 3, 2026",
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
fun HeadteacherSummaryGrid(pendingCount: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Attendance Rate",
                value = "93%",
                subText = "Week average",
                icon = Icons.Rounded.AssignmentTurnedIn,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Results Submitted",
                value = "68%",
                subText = "18 of 26 classes",
                icon = Icons.AutoMirrored.Rounded.Grading,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Staff Present",
                value = "22/24",
                subText = "2 on leave",
                icon = Icons.Rounded.SupervisorAccount,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Pending Approvals",
                value = pendingCount.toString(),
                subText = "Requires action",
                icon = Icons.Rounded.ReportProblem,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun WeeklyAttendanceTrendCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📈 Weekly Attendance Trend", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val days = listOf(
                "Monday" to 0.95f,
                "Tuesday" to 0.91f,
                "Wednesday" to 0.88f,
                "Thursday" to 0.93f,
                "Friday" to 0.89f
            )

            days.forEach { (name, progress) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(name, modifier = Modifier.width(80.dp), style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(8.dp)
                            .padding(horizontal = 8.dp),
                        color = if (progress > 0.9f) Color(0xFF4CAF50) else Color(0xFFBC9522),
                        trackColor = Color(0xFFEEEEEE),
                    )
                    Text("${(progress * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun PendingApprovalsCard(
    approvals: List<PendingApprovalItem>,
    onReviewClick: (PendingApprovalItem) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("⏳ Pending Approvals & Sign-offs", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            approvals.forEach { item ->
                ListItem(
                    headlineContent = { Text(item.title, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text(item.description) },
                    trailingContent = {
                        Button(
                            onClick = { onReviewClick(item) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (item.status.contains("Approved")) Color(0xFF4CAF50) else Color(0xFF1A3A6B)
                            ),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(if (item.status == "Pending") "Review" else item.status, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
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
fun HeadteacherDashboardPreview() {
    TasaagaOVCPSTheme {
        HeadteacherDashboardScreen(onLogout = {})
    }
}
