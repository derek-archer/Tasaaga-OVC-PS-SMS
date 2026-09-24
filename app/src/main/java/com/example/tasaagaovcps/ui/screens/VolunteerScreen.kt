package com.example.tasaagaovcps.ui.screens

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.Bed
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.R
import com.example.tasaagaovcps.data.model.VolunteerOpportunity
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.SubmissionState
import com.example.tasaagaovcps.ui.viewmodel.VolunteerViewModel

@Composable
fun VolunteerScreen(
    viewModel: VolunteerViewModel,
    modifier: Modifier = Modifier
) {
    val opportunities by viewModel.opportunities.collectAsState()
    val selectedOpportunity by viewModel.selectedOpportunity.collectAsState()
    val submissionState by viewModel.submissionState.collectAsState()

    VolunteerContent(
        opportunities = opportunities,
        onApplyClick = { viewModel.selectOpportunity(it) },
        modifier = modifier
    )

    if (selectedOpportunity != null) {
        VolunteerApplicationDialog(
            opportunity = selectedOpportunity!!,
            submissionState = submissionState,
            onSubmit = { name, email, message ->
                viewModel.submitApplication(name, email, message)
            },
            onDismiss = {
                viewModel.clearSelectedOpportunity()
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VolunteerContent(
    opportunities: List<VolunteerOpportunity>,
    onApplyClick: (VolunteerOpportunity) -> Unit,
    modifier: Modifier = Modifier
) {
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
                        text = "VOLUNTEER & MENTORSHIP",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFFFDD835),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Share Your Skills & Change Lives",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We welcome global and local educators, mentors, medical professionals, and skill trainers for short and long term placements in Sitabaale, Uganda.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }
            }
        }

        // CAMPUS IMAGE
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.tasaaga2),
                    contentDescription = "Volunteers working at Tasaaga",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }

        // OPPORTUNITIES SECTION HEADER
        item {
            Text(
                text = "Available Placements",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            )
        }

        items(opportunities) { opportunity ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = opportunity.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = opportunity.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF424242)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // Requirements Badges
                    if (opportunity.requirements.isNotEmpty()) {
                        Text("Requirements:", style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            opportunity.requirements.forEach { req ->
                                Surface(
                                    color = Color(0xFFE8F5E9),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(12.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(req, fontSize = 11.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Schedule, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFB71C1C))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(opportunity.duration, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold)
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Bed, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF1B5E20))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(opportunity.accommodation, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onApplyClick(opportunity) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB71C1C),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply Now", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun VolunteerApplicationDialog(
    opportunity: VolunteerOpportunity,
    submissionState: SubmissionState,
    onSubmit: (String, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var messageText by remember { mutableStateOf("") }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val isNameValid = name.isNotBlank()
    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isMessageValid = messageText.isNotBlank()

    AlertDialog(
        onDismissRequest = { if (submissionState !is SubmissionState.Loading) onDismiss() },
        title = {
            Text(
                text = if (submissionState is SubmissionState.Success) "Application Received!" else "Apply for ${opportunity.title}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                when (submissionState) {
                    is SubmissionState.Success -> {
                        Text(
                            text = "Thank you for your interest! Your application for '${opportunity.title}' has been submitted. Our volunteer coordination team will contact you at $email.",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    else -> {
                        if (submissionState is SubmissionState.Error) {
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = submissionState.message,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }

                        // Full Name Field
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Full Name *") },
                            isError = hasAttemptedSubmit && !isNameValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isNameValid) {
                                    Text("Full Name is required", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = submissionState !is SubmissionState.Loading,
                            singleLine = true
                        )

                        // Email Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address *") },
                            isError = hasAttemptedSubmit && !isEmailValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isEmailValid) {
                                    val errorMsg = if (email.isBlank()) "Email is required" else "Invalid email address"
                                    Text(errorMsg, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            enabled = submissionState !is SubmissionState.Loading,
                            singleLine = true
                        )

                        // Message Field
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            label = { Text("Statement of Purpose / Relevant Experience *") },
                            isError = hasAttemptedSubmit && !isMessageValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isMessageValid) {
                                    Text("Message cannot be blank", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3,
                            enabled = submissionState !is SubmissionState.Loading
                        )

                        if (submissionState is SubmissionState.Loading) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = Color(0xFFB71C1C))
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            if (submissionState is SubmissionState.Success) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Close")
                }
            } else {
                Button(
                    onClick = {
                        hasAttemptedSubmit = true
                        if (isNameValid && isEmailValid && isMessageValid) {
                            onSubmit(name, email, messageText)
                        }
                    },
                    enabled = submissionState !is SubmissionState.Loading,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Submit Application")
                }
            }
        },
        dismissButton = {
            if (submissionState !is SubmissionState.Success) {
                TextButton(
                    onClick = onDismiss,
                    enabled = submissionState !is SubmissionState.Loading
                ) {
                    Text("Cancel")
                }
            }
        }
    )
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun VolunteerScreenPreview() {
    TasaagaOVCPSTheme {
        VolunteerContent(
            opportunities = listOf(
                VolunteerOpportunity(
                    id = "1",
                    title = "Teaching Assistant",
                    description = "Assist primary school teachers in English and Mathematics.",
                    requirements = listOf("Fluent English", "Teaching experience preferred"),
                    duration = "3-6 months",
                    accommodation = "On-site guest house provided"
                ),
                VolunteerOpportunity(
                    id = "2",
                    title = "Mentorship Role",
                    description = "Provide career guidance and personal mentorship to vocational students.",
                    requirements = listOf("Professional background", "Strong communication skills"),
                    duration = "Flexible",
                    accommodation = "On-site guest house provided"
                )
            ),
            onApplyClick = {}
        )
    }
}
