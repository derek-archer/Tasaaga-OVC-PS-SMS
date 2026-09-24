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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Logout
import androidx.compose.material.icons.automirrored.rounded.ReceiptLong
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.ui.components.SummaryCard
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme

data class FinancePaymentItem(
    val receiptNo: String,
    val studentName: String,
    val amount: Double,
    val paymentMethod: String,
    val date: String = "2026-09-22"
)

data class FinanceExpenseItem(
    val category: String,
    val description: String,
    val amount: Double,
    val status: String = "Pending Approval"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinanceDashboardScreen(
    onLogout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val paymentsList = remember {
        mutableStateListOf(
            FinancePaymentItem("RCP-2026-1047", "Achola Mary", 450000.0, "Cash"),
            FinancePaymentItem("RCP-2026-1046", "Mugisha Peter", 380000.0, "MTN Mobile Money"),
            FinancePaymentItem("RCP-2026-1045", "Nakato Esther", 450000.0, "Airtel Money")
        )
    }

    val expensesList = remember {
        mutableStateListOf(
            FinanceExpenseItem("Teaching Materials", "P.5 and P.6 Exercise Books", 450000.0, "Approved"),
            FinanceExpenseItem("Utilities", "Electricity Bill - August", 280000.0, "Approved"),
            FinanceExpenseItem("Maintenance", "Classroom Roof Repair", 850000.0, "Pending Approval")
        )
    }

    var showRecordPaymentDialog by remember { mutableStateOf(false) }
    var showRecordExpenseDialog by remember { mutableStateOf(false) }

    var totalCollected by remember { mutableDoubleStateOf(32500000.0 + paymentsList.sumOf { it.amount }) }

    // 1. RECORD PAYMENT DIALOG
    if (showRecordPaymentDialog) {
        var studentName by remember { mutableStateOf("Achola Mary") }
        var amountText by remember { mutableStateOf("180000") }
        var paymentMethod by remember { mutableStateOf("Cash") }
        var referenceNo by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showRecordPaymentDialog = false },
            title = { Text("Record Fee Payment", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = studentName,
                        onValueChange = { studentName = it },
                        label = { Text("Student Name *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Amount Paid (UGX) *") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    Text("Payment Method:", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                    val methods = listOf("Cash", "MTN Mobile Money", "Airtel Money", "Bank Transfer")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        methods.forEach { m ->
                            FilterChip(
                                selected = paymentMethod == m,
                                onClick = { paymentMethod = m },
                                label = { Text(m, fontSize = 10.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }

                    OutlinedTextField(
                        value = referenceNo,
                        onValueChange = { referenceNo = it },
                        label = { Text("Transaction Reference No. (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 180000.0
                        val receipt = "RCP-2026-${(1048..1999).random()}"
                        paymentsList.add(0, FinancePaymentItem(receipt, studentName, amt, paymentMethod))
                        totalCollected += amt
                        showRecordPaymentDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B))
                ) {
                    Text("Confirm & Print Receipt", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecordPaymentDialog = false }) { Text("Cancel") }
            }
        )
    }

    // 2. RECORD EXPENSE DIALOG
    if (showRecordExpenseDialog) {
        var category by remember { mutableStateOf("Teaching Materials") }
        var description by remember { mutableStateOf("") }
        var amountText by remember { mutableStateOf("150000") }

        AlertDialog(
            onDismissRequest = { showRecordExpenseDialog = false },
            title = { Text("Record Requisition / Expense", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = { category = it },
                        label = { Text("Category (e.g. Materials, Utilities, Food)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Expense Description *") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )

                    OutlinedTextField(
                        value = amountText,
                        onValueChange = { amountText = it },
                        label = { Text("Amount Requested (UGX) *") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val amt = amountText.toDoubleOrNull() ?: 150000.0
                        expensesList.add(0, FinanceExpenseItem(category, description, amt, "Pending Approval"))
                        showRecordExpenseDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B))
                ) {
                    Text("Submit Requisition", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRecordExpenseDialog = false }) { Text("Cancel") }
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
            FinanceHeader(onLogout)
        }

        item {
            FinanceActionGrid(
                onRecordPayment = { showRecordPaymentDialog = true },
                onRecordExpense = { showRecordExpenseDialog = true }
            )
        }

        item {
            FinanceSummaryGrid(collectedTotal = totalCollected)
        }

        item {
            RecentPaymentsCard(payments = paymentsList)
        }

        item {
            ExpensesCard(expenses = expensesList)
        }

        item {
            PaymentMethodsCard()
        }
    }
}

@Composable
fun FinanceHeader(onLogout: () -> Unit) {
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
                Text("💰", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Finance Dashboard",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
                Text(
                    text = "Mr. Okot David • Finance Officer",
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
fun FinanceActionGrid(
    onRecordPayment: () -> Unit,
    onRecordExpense: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Button(
            onClick = onRecordPayment,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A3A6B)),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.Rounded.Payments, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Record Payment", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }

        Button(
            onClick = onRecordExpense,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBC9522)),
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Icon(Icons.AutoMirrored.Rounded.ReceiptLong, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("Record Expense", fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun FinanceSummaryGrid(collectedTotal: Double) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SummaryCard(
                title = "Total Assessed",
                value = "45M",
                subText = "UGX 45,000,000",
                icon = Icons.AutoMirrored.Rounded.ReceiptLong,
                color = Color(0xFFBC9522),
                modifier = Modifier.weight(1f)
            )
            SummaryCard(
                title = "Collected",
                value = "UGX ${"%,.0f".format(collectedTotal)}",
                subText = "Live updated total",
                icon = Icons.Rounded.AccountBalanceWallet,
                color = Color(0xFF4CAF50),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun RecentPaymentsCard(payments: List<FinancePaymentItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("💳 Recent Payments & Receipts", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            payments.forEach { p ->
                Row(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(p.studentName, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text("${p.receiptNo} • ${p.paymentMethod}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Text("UGX ${"%,.0f".format(p.amount)}", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodySmall)
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun ExpensesCard(expenses: List<FinanceExpenseItem>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📋 Recent Expense Requisitions", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            expenses.forEach { ex ->
                Row(
                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ex.category, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                        Text(ex.description, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("UGX ${"%,.0f".format(ex.amount)}", fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C), style = MaterialTheme.typography.bodySmall)
                        Text(ex.status, style = MaterialTheme.typography.labelSmall, color = if (ex.status.contains("Approved")) Color(0xFF4CAF50) else Color(0xFFF57F17))
                    }
                }
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            }
        }
    }
}

@Composable
fun PaymentMethodsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("📊 Payment Methods Distribution", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(12.dp))

            val methods = listOf(
                "Cash" to 0.57f,
                "MTN MoMo" to 0.25f,
                "Airtel Money" to 0.18f
            )

            methods.forEach { (name, ratio) ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(name, modifier = Modifier.width(80.dp), style = MaterialTheme.typography.bodySmall)
                    LinearProgressIndicator(
                        progress = { ratio },
                        modifier = Modifier.weight(1f).height(6.dp).padding(horizontal = 8.dp),
                        color = Color(0xFF1A3A6B),
                        trackColor = Color(0xFFEEEEEE),
                    )
                    Text("${(ratio * 100).toInt()}%", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinanceDashboardPreview() {
    TasaagaOVCPSTheme {
        FinanceDashboardScreen(onLogout = {})
    }
}
