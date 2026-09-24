package com.example.tasaagaovcps.ui.screens

import android.util.Patterns
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
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.data.model.DonationInfo
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.SupportViewModel

@Composable
fun SupportScreen(
    viewModel: SupportViewModel,
    modifier: Modifier = Modifier
) {
    val donationInfo by viewModel.donationInfo.collectAsState()

    donationInfo?.let { info ->
        SupportContent(info = info, modifier = modifier)
    } ?: Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFFB71C1C))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportContent(
    info: DonationInfo,
    modifier: Modifier = Modifier
) {
    var selectedMethodForDonation by remember { mutableStateOf<String?>(null) }
    var donorName by remember { mutableStateOf("") }
    var donorEmail by remember { mutableStateOf("") }
    var donationAmount by remember { mutableStateOf("180000") }
    var sponsorshipNote by remember { mutableStateOf("") }

    var hasAttemptedSubmit by remember { mutableStateOf(false) }
    var showThankYouDialog by remember { mutableStateOf(false) }

    val isNameValid = donorName.isNotBlank()
    val isEmailValid = donorEmail.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(donorEmail).matches()
    val isAmountValid = donationAmount.toDoubleOrNull()?.let { it > 0 } ?: false

    if (showThankYouDialog) {
        AlertDialog(
            onDismissRequest = { showThankYouDialog = false },
            title = { Text("Thank You for Your Generosity!") },
            text = { Text("Your sponsorship/donation pledge of UGX ${donationAmount.toDoubleOrNull()?.let { "%,.0f".format(it) } ?: donationAmount} via $selectedMethodForDonation has been recorded. Our development office will send a formal receipt to $donorEmail.") },
            confirmButton = {
                Button(
                    onClick = {
                        showThankYouDialog = false
                        selectedMethodForDonation = null
                        donorName = ""
                        donorEmail = ""
                        sponsorshipNote = ""
                        hasAttemptedSubmit = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Close")
                }
            }
        )
    }

    if (selectedMethodForDonation != null && !showThankYouDialog) {
        AlertDialog(
            onDismissRequest = { selectedMethodForDonation = null },
            title = { Text("Pledge via $selectedMethodForDonation", fontWeight = FontWeight.Bold) },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = donorName,
                        onValueChange = { donorName = it },
                        label = { Text("Donor / Sponsor Full Name *") },
                        isError = hasAttemptedSubmit && !isNameValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isNameValid) {
                                Text("Name cannot be blank", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = donorEmail,
                        onValueChange = { donorEmail = it },
                        label = { Text("Email Address for Receipt *") },
                        isError = hasAttemptedSubmit && !isEmailValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isEmailValid) {
                                val msg = if (donorEmail.isBlank()) "Email is required" else "Invalid email address"
                                Text(msg, color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = donationAmount,
                        onValueChange = { donationAmount = it },
                        label = { Text("Amount (UGX) *") },
                        isError = hasAttemptedSubmit && !isAmountValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isAmountValid) {
                                Text("Enter a valid amount in UGX", color = MaterialTheme.colorScheme.error)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = sponsorshipNote,
                        onValueChange = { sponsorshipNote = it },
                        label = { Text("Sponsorship / Child Preference Note (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        hasAttemptedSubmit = true
                        if (isNameValid && isEmailValid && isAmountValid) {
                            showThankYouDialog = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Confirm Pledge")
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedMethodForDonation = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // HERO HEADER
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFB71C1C)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "HELP A CHILD LEARN & THRIVE",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFFFDD835),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = info.title,
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = info.description,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }
            }
        }

        // SPONSORSHIP TIERS
        item {
            Column {
                Text(
                    text = "Featured Sponsorship Tiers",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))

                val tiers = listOf(
                    SponsorshipTier("Sponsor a Student", "UGX 180,000 / term", "Covers tuition, textbooks, daily meals, and basic care for an OVC student.", Icons.Rounded.School, Color(0xFFD32F2F)),
                    SponsorshipTier("Musawo Health Fund", "UGX 100,000 / term", "Provides healthcare, first aid, and medical checkups at Musawo Clinic.", Icons.Rounded.LocalHospital, Color(0xFF2E7D32)),
                    SponsorshipTier("Scholastic Supplies", "UGX 50,000 / term", "Provides exercise books, pens, school bag, and uniforms.", Icons.AutoMirrored.Rounded.MenuBook, Color(0xFF1976D2))
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    tiers.forEach { tier ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    donationAmount = tier.amount.replace(Regex("[^0-9]"), "")
                                    selectedMethodForDonation = "Mobile Money"
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(tier.color.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(tier.icon, contentDescription = null, tint = tier.color, modifier = Modifier.size(24.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(tier.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text(tier.amount, fontWeight = FontWeight.ExtraBold, color = Color(0xFF1B5E20), fontSize = 12.sp)
                                    }
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(tier.desc, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                                }
                            }
                        }
                    }
                }
            }
        }

        // SPONSORSHIP DETAILS SUMMARY
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "OVC Sponsorship Scope",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = info.sponsorshipDetails,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF424242))
                    )
                }
            }
        }

        // PAYMENT METHODS LIST
        item {
            Text(
                text = "Supported Payment Channels",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            )
        }

        items(info.donationMethods) { method ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                ListItem(
                    headlineContent = { Text(method, fontWeight = FontWeight.Bold) },
                    supportingContent = { Text("Instant pledge and receipt issuance for $method.") },
                    leadingContent = {
                        Icon(
                            imageVector = if (method.contains("Online", ignoreCase = true) || method.contains("Credit", ignoreCase = true)) Icons.Rounded.CreditCard else Icons.Rounded.PhoneAndroid,
                            contentDescription = null,
                            tint = Color(0xFFB71C1C)
                        )
                    },
                    trailingContent = {
                        Button(
                            onClick = {
                                selectedMethodForDonation = method
                                hasAttemptedSubmit = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFB71C1C),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Donate", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp))
                        }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}

private data class SponsorshipTier(
    val name: String,
    val amount: String,
    val desc: String,
    val icon: ImageVector,
    val color: Color
)

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SupportScreenPreview() {
    TasaagaOVCPSTheme {
        SupportContent(
            info = DonationInfo(
                title = "Empower Through Giving",
                description = "We offer subsidized fees for those who can afford a small contribution, but many of our students are Orphans and Vulnerable Children (OVC) who rely entirely on sponsorship.",
                donationMethods = listOf("Online Bank Transfer", "MTN Mobile Money", "Airtel Money", "International Credit Card"),
                sponsorshipDetails = "Sponsoring a child covers tuition, boarding, meals, and medical care at Musawo Clinic."
            )
        )
    }
}
