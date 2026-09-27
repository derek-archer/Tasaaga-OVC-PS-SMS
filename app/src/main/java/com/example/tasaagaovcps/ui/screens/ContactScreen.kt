package com.example.tasaagaovcps.ui.screens

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider
import com.example.tasaagaovcps.ui.viewmodel.ContactUiState
import com.example.tasaagaovcps.ui.viewmodel.ContactViewModel

@Composable
fun ContactScreen(
    modifier: Modifier = Modifier,
    contactViewModel: ContactViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val uiState by contactViewModel.uiState.collectAsState()

    val isNameValid = name.isNotBlank()
    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isMessageValid = message.isNotBlank()
    val isSending = uiState is ContactUiState.Sending

    // Success dialog
    if (uiState is ContactUiState.Success) {
        AlertDialog(
            onDismissRequest = {
                contactViewModel.resetState()
                name = ""; email = ""; phone = ""; subject = ""; message = ""
                hasAttemptedSubmit = false
            },
            title = { Text("Message Sent!") },
            text = {
                Text(
                    "Thank you for reaching out to Tasaaga OVC Primary School. " +
                    "Our administration will reply to $email shortly."
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        contactViewModel.resetState()
                        name = ""; email = ""; phone = ""; subject = ""; message = ""
                        hasAttemptedSubmit = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) { Text("Close") }
            }
        )
    }

    // Error dialog
    if (uiState is ContactUiState.Error) {
        val errorMsg = (uiState as ContactUiState.Error).message
        AlertDialog(
            onDismissRequest = { contactViewModel.resetState() },
            title = { Text("Send Failed") },
            text = { Text("Could not send your message: $errorMsg\n\nPlease try again or email us directly at info@tasaagaschool.org.") },
            confirmButton = {
                Button(onClick = { contactViewModel.resetState() }) { Text("OK") }
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
                        text = "GET IN TOUCH",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFFFDD835),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Contact & Visit Us",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We welcome visitors, prospective parents, donors, and volunteers at our campus in Sitabaale, Uganda.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }
            }
        }

        // CONTACT INFO
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    ContactInfoRow(
                        icon = { Icon(Icons.Rounded.LocationOn, "Location", tint = Color(0xFF1B5E20)) },
                        iconBg = Color(0xFFE8F5E9),
                        label = "Campus Location",
                        value = "Sitabaale, Kiwenda, Wakiso District, Uganda"
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    ContactInfoRow(
                        icon = { Icon(Icons.Rounded.Phone, "Phone", tint = Color(0xFFB71C1C)) },
                        iconBg = Color(0xFFFFEBEE),
                        label = "Phone Number",
                        value = "+256 0789 532 928  /  +256 0701 234 567"
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    ContactInfoRow(
                        icon = { Icon(Icons.Rounded.Email, "Email", tint = Color(0xFF1565C0)) },
                        iconBg = Color(0xFFE3F2FD),
                        label = "Email Address",
                        value = "info@tasaagaschool.org"
                    )
                    HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
                    ContactInfoRow(
                        icon = { Icon(Icons.Rounded.AccessTime, "Hours", tint = Color(0xFFF57F17)) },
                        iconBg = Color(0xFFFFF8E1),
                        label = "Office Hours",
                        value = "Monday - Friday: 8:00 AM - 5:00 PM EAT"
                    )
                }
            }
        }

        // INQUIRY FORM
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Send Us an Inquiry",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Your Full Name *") },
                        isError = hasAttemptedSubmit && !isNameValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isNameValid)
                                Text("Name is required", color = MaterialTheme.colorScheme.error)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isSending
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email Address *") },
                        isError = hasAttemptedSubmit && !isEmailValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isEmailValid)
                                Text(
                                    if (email.isBlank()) "Email is required" else "Invalid email address",
                                    color = MaterialTheme.colorScheme.error
                                )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        enabled = !isSending
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone Number (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        enabled = !isSending
                    )

                    OutlinedTextField(
                        value = subject,
                        onValueChange = { subject = it },
                        label = { Text("Subject / Purpose of Visit") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !isSending
                    )

                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Your Message *") },
                        isError = hasAttemptedSubmit && !isMessageValid,
                        supportingText = {
                            if (hasAttemptedSubmit && !isMessageValid)
                                Text("Message is required", color = MaterialTheme.colorScheme.error)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        enabled = !isSending
                    )

                    Button(
                        onClick = {
                            hasAttemptedSubmit = true
                            if (isNameValid && isEmailValid && isMessageValid) {
                                contactViewModel.sendInquiry(name, email, phone, subject, message)
                            }
                        },
                        modifier = Modifier.align(Alignment.End),
                        enabled = !isSending,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB71C1C),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        if (isSending) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Sending…", fontWeight = FontWeight.Bold)
                        } else {
                            Icon(
                                Icons.AutoMirrored.Rounded.Send,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send Message", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ContactInfoRow(
    icon: @Composable () -> Unit,
    iconBg: Color,
    label: String,
    value: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(iconBg, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) { icon() }
        Column {
            Text(text = label, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
        }
    }
}
