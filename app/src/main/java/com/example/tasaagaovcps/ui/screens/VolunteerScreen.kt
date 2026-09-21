package com.example.tasaagaovcps.ui.screens

import android.util.Patterns
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bed
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasaagaovcps.data.model.VolunteerOpportunity
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.SubmissionState
import com.example.tasaagaovcps.ui.viewmodel.VolunteerViewModel

import androidx.compose.foundation.Image
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.tasaagaovcps.R

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

@Composable
fun VolunteerContent(
    opportunities: List<VolunteerOpportunity>,
    onApplyClick: (VolunteerOpportunity) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.tasaaga2),
                contentDescription = "Volunteers working at Tasaaga",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
        }

        item {
            Text(
                text = "Volunteer Opportunities",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Text(
                text = "Join us in making a difference. We welcome mentors and educators for 3-6 month commitments.",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        items(opportunities) { opportunity ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = opportunity.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = opportunity.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Schedule,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Duration: ${opportunity.duration}", style = MaterialTheme.typography.bodySmall)
                    }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Bed,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Accommodation: ${opportunity.accommodation}", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { onApplyClick(opportunity) },
                        modifier = Modifier.align(Alignment.End),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text("Apply Now")
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
                text = if (submissionState is SubmissionState.Success) "Application Sent!" else "Apply for ${opportunity.title}",
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
                            text = "Thank you for your interest! Your application for '${opportunity.title}' has been successfully submitted. We will contact you soon.",
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
                            label = { Text("Full Name") },
                            isError = hasAttemptedSubmit && !isNameValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isNameValid) {
                                    Text("Full Name cannot be blank", color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = submissionState !is SubmissionState.Loading
                        )

                        // Email Field
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email Address") },
                            isError = hasAttemptedSubmit && !isEmailValid,
                            supportingText = {
                                if (hasAttemptedSubmit && !isEmailValid) {
                                    val errorMsg = if (email.isBlank()) "Email cannot be blank" else "Invalid email address format"
                                    Text(errorMsg, color = MaterialTheme.colorScheme.error)
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = submissionState !is SubmissionState.Loading
                        )

                        // Message Field
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            label = { Text("Message / Statement of Purpose") },
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
                                CircularProgressIndicator()
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
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
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
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
                    Text("Cancel", color = MaterialTheme.colorScheme.secondary)
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
