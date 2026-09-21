package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
        CircularProgressIndicator()
    }
}

@Composable
fun SupportContent(
    info: DonationInfo,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Support & Donations",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = info.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = info.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        item {
            Text(
                text = "OVC Sponsorship",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = info.sponsorshipDetails,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Text(
                text = "How to Donate",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        items(info.donationMethods) { method ->
            ListItem(
                headlineContent = { Text(method) },
                leadingContent = {
                    Icon(
                        imageVector = if (method.contains("Online", ignoreCase = true)) Icons.Rounded.Payments else Icons.Rounded.CardGiftcard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.secondary
                    )
                },
                trailingContent = {
                    Button(
                        onClick = { /* Mock donation action */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    ) {
                        Text("Donate")
                    }
                }
            )
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun SupportScreenPreview() {
    TasaagaOVCPSTheme {
        SupportContent(
            info = DonationInfo(
                title = "Empower Through Giving",
                description = "We offer subsidized fees for those who can afford a small contribution, but many of our students are Orphans and Vulnerable Children (OVC) who rely entirely on sponsorship.",
                donationMethods = listOf("Online Bank Transfer", "Mobile Money", "International Wire"),
                sponsorshipDetails = "Sponsoring a child covers tuition, boarding, meals, and medical care at Musawo Clinic."
            )
        )
    }
}
