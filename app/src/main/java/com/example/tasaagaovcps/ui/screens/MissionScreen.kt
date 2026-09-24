package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.R
import com.example.tasaagaovcps.data.model.SchoolInfo
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.SchoolViewModel

@Composable
fun MissionScreen(
    viewModel: SchoolViewModel,
    onNavigateToAdmissions: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val schoolInfo by viewModel.schoolInfo.collectAsState()

    schoolInfo?.let { info ->
        MissionContent(info = info, onNavigateToAdmissions = onNavigateToAdmissions, modifier = modifier)
    } ?: Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color(0xFFB71C1C))
    }
}

@Composable
fun MissionContent(
    info: SchoolInfo,
    onNavigateToAdmissions: () -> Unit = {},
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
                        text = "OUR MISSION & PROGRAMMES",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFFFDD835),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Nurturing Mind, Body & Spirit",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = info.mission,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }
            }
        }

        // MOTTO & QUOTE
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(48.dp)
                            .background(Color(0xFFFDD835), RoundedCornerShape(2.dp))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "MOTTO: ${info.motto.uppercase()}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFF1B5E20),
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "“Cultivating true independence and self-reliance for orphaned & vulnerable children.”",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF111111)
                            )
                        )
                    }
                }
            }
        }

        // PROGRAMMES SECTION HEADER
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Core School Programmes",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )
                Text(
                    text = "Day & Boarding",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF757575))
                )
            }
        }

        // 4 PROGRAMME CARDS
        item {
            val coreProgrammes = listOf(
                ProgrammeDetail("Primary Education", "Top-tier, inclusive primary education from P.1 to P.7 with dedicated teachers.", Icons.AutoMirrored.Rounded.MenuBook, Color(0xFFD32F2F)),
                ProgrammeDetail("Day & Boarding", "Safe, comfortable dormitories and a supportive home away from home.", Icons.Rounded.Home, Color(0xFF2E7D32)),
                ProgrammeDetail("Meals & Wellbeing", "Nutritious daily meals, clean water, and healthcare via Musawo Clinic.", Icons.Rounded.Restaurant, Color(0xFFFBC02D)),
                ProgrammeDetail("Life Skills & Crafts", "Vocational training, carpentry, tailoring, and computer literacy.", Icons.Rounded.Engineering, Color(0xFF1976D2))
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                coreProgrammes.forEach { p ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
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
                                    .background(p.color.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(p.icon, contentDescription = null, tint = p.color, modifier = Modifier.size(24.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(p.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(p.desc, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }

        // LIVE SUPABASE PROGRAMMES LIST
        item {
            Text(
                text = "Configured Academic Pathways",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B5E20)
                )
            )
        }

        items(info.programs) { program ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                ListItem(
                    headlineContent = { Text(program, fontWeight = FontWeight.Bold) },
                    overlineContent = { Text("OFFERED PROGRAMME", color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold, fontSize = 10.sp) },
                    supportingContent = {
                        Text(
                            when {
                                program.contains("Primary", ignoreCase = true) -> "Full national curriculum with day & boarding enrollment options."
                                program.contains("Secondary", ignoreCase = true) -> "Secondary transition pathway focusing on academic excellence."
                                else -> "Hands-on vocational training empowering youth self-reliance."
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    },
                    leadingContent = {
                        Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32))
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }

        // LIVE CALL TO ACTION BUTTON
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Ready to enroll your child at Tasaaga?",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        ),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = onNavigateToAdmissions,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enquire for Admission", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

private data class ProgrammeDetail(
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val color: Color
)

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
