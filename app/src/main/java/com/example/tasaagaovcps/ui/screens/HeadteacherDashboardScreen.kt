package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasaagaovcps.data.model.ExpenseRecord
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider
import com.example.tasaagaovcps.ui.viewmodel.HeadteacherViewModel

@Composable
fun HeadteacherDashboardScreen(
    schoolId: String,
    onLogout: () -> Unit,
    modifier: Modifier = Modifier,
    headteacherViewModel: HeadteacherViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by headteacherViewModel.state.collectAsState()

    LaunchedEffect(schoolId) {
        headteacherViewModel.load(schoolId)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { HeadteacherHeader(onLogout) }

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
                HeadteacherSummaryGrid(
                    attendanceRate = state.attendanceRate,
                    activeStaff = state.activeStaff,
                    staffOnLeave = state.staffOnLeave,
                    pendingExpensesCount = state.pendingExpenses.size
                )
            }
        }

        if (!state.isLoading && state.pendingExpenses.isNotEmpty()) {
            item {
                PendingExpensesCard(expenses = state.pendingExpenses)
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

@Composable
fun HeadteacherHeader(onLogout: () -> Unit) {
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
fun HeadteacherSummaryGrid(
    attendanceRate: Double,
    activeStaff: Int,
    staffOnLeave: Int,
    pendingExpensesCount: Int
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Attendance Rate",
                value = "${"%.0f".format(attendanceRate)}%",
                subText = "School-wide average",
                icon = Icons.Rounded.AssignmentTurnedIn,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Active Staff",
                value = activeStaff.toString(),
                subText = "$staffOnLeave on leave",
                icon = Icons.Rounded.SupervisorAccount,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Staff on Leave",
                value = staffOnLeave.toString(),
                subText = "Currently absent",
                icon = Icons.AutoMirrored.Rounded.Grading,
                color = Color(0xFF2196F3),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Pending Expenses",
                value = pendingExpensesCount.toString(),
                subText = "Requires approval",
                icon = Icons.Rounded.ReportProblem,
                color = Color(0xFFF44336),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PendingExpensesCard(expenses: List<ExpenseRecord>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("⏳ Pending Expense Approvals", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            expenses.take(10).forEach { expense ->
                ListItem(
                    headlineContent = { Text(expense.description ?: expense.category, fontWeight = FontWeight.Bold) },
                    supportingContent = {
                        Text("UGX %,.0f • ${expense.category}".format(expense.amount))
                    },
                    trailingContent = {
                        Surface(
                            color = Color(0xFFFFF8E1),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                expense.status,
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFFBC9522),
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
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
        HeadteacherDashboardScreen(schoolId = "preview", onLogout = {})
    }
}
