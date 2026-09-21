package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasaagaovcps.data.model.SchoolInfo
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.SchoolViewModel

@Composable
fun MissionScreen(
    viewModel: SchoolViewModel,
    modifier: Modifier = Modifier
) {
    val schoolInfo by viewModel.schoolInfo.collectAsState()

    schoolInfo?.let { info ->
        MissionContent(info = info, modifier = modifier)
    } ?: Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun MissionContent(
    info: SchoolInfo,
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
                text = "Mission & Programs",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Our Motto",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = info.motto,
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Our Mission",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = info.mission,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }

        item {
            Text(
                text = "Educational Programs",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        items(info.programs) { program ->
            ListItem(
                headlineContent = { Text(program) },
                overlineContent = { Text("Program") },
                supportingContent = {
                    Text(
                        when {
                            program.contains("Primary", ignoreCase = true) -> "Day and Boarding options available."
                            program.contains("Secondary", ignoreCase = true) -> "Focus on academic excellence and vocational skills."
                            else -> "Empowering our students for the future."
                        }
                    )
                }
            )
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun MissionScreenPreview() {
    TasaagaOVCPSTheme {
        MissionContent(
            info = SchoolInfo(
                mission = "To provide quality education and support to orphaned and vulnerable children, fostering self-reliance.",
                motto = "Rising To Succeed",
                programs = listOf("Primary (Day/Boarding)", "Secondary/Vocational")
            )
        )
    }
}
