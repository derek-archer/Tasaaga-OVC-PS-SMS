package com.example.tasaagaovcps.ui.screens

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.Send
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tasaagaovcps.R
import com.example.tasaagaovcps.data.model.NewsItem
import com.example.tasaagaovcps.ui.components.TasaagaLogo
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.AppViewModelProvider
import com.example.tasaagaovcps.ui.viewmodel.NewsViewModel

@Composable
fun HomeScreen(
    onNavigateToLogin: () -> Unit = {},
    onNavigateToAdmissions: () -> Unit = {},
    onNavigateToDonate: () -> Unit = {},
    onNavigateToVolunteer: () -> Unit = {},
    modifier: Modifier = Modifier,
    newsViewModel: NewsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val newsItems by newsViewModel.newsItems.collectAsState()

    var faqExpandedIndex by remember { mutableStateOf<Int?>(null) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var agreementChecked by remember { mutableStateOf(true) }

    var hasAttemptedSubmit by remember { mutableStateOf(false) }
    var showInquirySuccess by remember { mutableStateOf(false) }

    val isNameValid = name.isNotBlank()
    val isEmailValid = email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isMessageValid = message.isNotBlank()

    if (showInquirySuccess) {
        AlertDialog(
            onDismissRequest = { showInquirySuccess = false },
            title = { Text("Admissions Inquiry Received!") },
            text = { Text("Thank you, $name. Our admissions team at Tasaaga Primary School will review your inquiry and contact you shortly.") },
            confirmButton = {
                Button(
                    onClick = {
                        showInquirySuccess = false
                        name = ""
                        email = ""
                        message = ""
                        hasAttemptedSubmit = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
                ) {
                    Text("Close")
                }
            }
        )
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9)),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. TOP HEADER BANNER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFB71C1C)), // Brand Red
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "TASAAGA PRIMARY SCHOOL",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = Color.White,
                                fontWeight = FontWeight.Black
                            )
                        )
                        Text(
                            text = "Rising To Succeed • Day & Boarding OVC",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color(0xFFFDD835),
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = onNavigateToLogin,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFFDD835), // Brand Gold / Yellow
                            contentColor = Color(0xFF111111)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Rounded.AccountCircle,
                                contentDescription = "Portal Login",
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Portal login",
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false
                            )
                        }
                    }
                }
            }
        }

        // 2. HERO SECTION
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "QUALITY EDUCATION • CARING COMMUNITY • BRIGHTER FUTURES",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(0xFF757575),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "A brighter future\nstarts here.",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 32.sp,
                        lineHeight = 38.sp,
                        color = Color(0xFF111111)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Learning, care and opportunity in Sitabaale, Uganda.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = Color(0xFF424242),
                        fontWeight = FontWeight.Medium
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onNavigateToAdmissions,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFB71C1C),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Explore our school", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    OutlinedButton(
                        onClick = onNavigateToAdmissions,
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF111111))
                    ) {
                        Text("Admissions enquiry", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.tasaaga3),
                        contentDescription = "Tasaaga Campus",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Illustrative image",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // 3. WELCOME & QUOTE CARD
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFDE7)), // Light gold tint
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "WELCOME TO TASAAGA",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFF2E7D32),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Rising To Succeed",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = Color(0xFFB71C1C),
                            fontWeight = FontWeight.Black
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Located in Sitabaale, Uganda, Tasaaga Primary School provides top-tier inclusive education, full meals, and lifeskills for orphaned and vulnerable children, cultivating true independence and self-reliance.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF424242))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(12.dp),
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
                                text = "“Every child deserves a chance to learn, belong and succeed.”",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic,
                                    color = Color(0xFF111111)
                                )
                            )
                            Text(
                                text = "TASAAGA PRIMARY SCHOOL",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF757575),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }
            }
        }

        // 4. OUR PROGRAMMES
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Our Programmes",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    )
                    Text(
                        text = "Holistic support",
                        style = MaterialTheme.typography.labelMedium.copy(color = Color(0xFF757575))
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                val programmes = listOf(
                    ProgrammeItem("Primary education", "Quality, inclusive learning for every child.", Icons.AutoMirrored.Rounded.MenuBook, Color(0xFFD32F2F)),
                    ProgrammeItem("Day & boarding", "A safe and supportive home away from home.", Icons.Rounded.Home, Color(0xFF2E7D32)),
                    ProgrammeItem("Meals & wellbeing", "Nutritious meals and caring support.", Icons.Rounded.Restaurant, Color(0xFFFBC02D)),
                    ProgrammeItem("Life skills", "Building confidence, independence and brighter futures.", Icons.Rounded.People, Color(0xFF1976D2))
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    programmes.chunked(2).forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            rowItems.forEach { p ->
                                Card(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(130.dp),
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Icon(p.icon, contentDescription = null, tint = p.color, modifier = Modifier.size(28.dp))
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(p.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                        Text(p.desc, style = MaterialTheme.typography.bodySmall, color = Color.Gray, fontSize = 11.sp, lineHeight = 14.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. ADMISSIONS JOURNEY
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Start your child’s journey",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1B5E20)
                        )
                    )
                    Text(
                        text = "A simple process, a brighter tomorrow.",
                        style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF424242))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val steps = listOf(
                        "1" to "Enquire" to "Get in touch and ask questions",
                        "2" to "Visit" to "Come and see our school campus",
                        "3" to "Apply" to "Complete the enrollment form",
                        "4" to "Review" to "We will confirm admission"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        steps.forEach { step ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .background(Color(0xFF2E7D32), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(step.first.first, color = Color.White, fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(step.first.second, fontWeight = FontWeight.Bold, fontSize = 11.sp, textAlign = TextAlign.Center)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onNavigateToAdmissions,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Ask about admissions", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // 6. COMMUNITY & SUPPORT
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Help a child learn and thrive",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )
                Text(
                    text = "Your support helps us provide education, care and opportunity.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF757575))
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SupportMiniCard("Sponsor learning", "Help a child access education.", Icons.Rounded.School, Color(0xFFD32F2F), Modifier.weight(1f), onNavigateToDonate)
                    SupportMiniCard("Volunteer skills", "Share time & knowledge.", Icons.Rounded.Handshake, Color(0xFF2E7D32), Modifier.weight(1f), onNavigateToVolunteer)
                    SupportMiniCard("Support essentials", "Help with learning supplies.", Icons.Rounded.Favorite, Color(0xFFFBC02D), Modifier.weight(1f), onNavigateToDonate)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNavigateToDonate,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Discuss how to help", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }

        // 7. DYNAMIC NEWS & NOTICES
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "School news and notices",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )
                Text(
                    text = "Stay informed with the latest from our school.",
                    style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF757575))
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (newsItems.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFB71C1C))
                    }
                } else {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(newsItems) { news ->
                            NewsCard(news)
                        }
                    }
                }
            }
        }

        // 8. FAQ ACCORDION & LIVE INQUIRY FORM
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "Frequently asked questions",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1B5E20)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                val faqs = listOf(
                    "How do I apply?" to "Applications can be submitted online via our enquiry form below or in person at the school administration office in Sitabaale, Uganda.",
                    "Is boarding available?" to "Yes, we offer secure and supportive boarding facilities for both boys and girls from P.1 to P.7 with full meals and healthcare.",
                    "How can I help?" to "You can sponsor an OVC student, volunteer your skills, or donate essential scholastic materials through our support portal."
                )

                faqs.forEachIndexed { index, faq ->
                    val isExpanded = faqExpandedIndex == index
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                faqExpandedIndex = if (isExpanded) null else index
                            },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(faq.first, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                Icon(
                                    imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                                    contentDescription = null
                                )
                            }
                            AnimatedVisibility(visible = isExpanded) {
                                Text(
                                    text = faq.second,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // LIVE INQUIRY FORM
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Let’s talk",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1B5E20)
                            )
                        )
                        Text(
                            text = "We’d love to hear from you. Send us an enquiry and we’ll get back to you.",
                            style = MaterialTheme.typography.bodySmall.copy(color = Color(0xFF757575))
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Name *") },
                            isError = hasAttemptedSubmit && !isNameValid,
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email *") },
                            isError = hasAttemptedSubmit && !isEmailValid,
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = message,
                            onValueChange = { message = it },
                            label = { Text("Message / Enquiry *") },
                            isError = hasAttemptedSubmit && !isMessageValid,
                            modifier = Modifier.fillMaxWidth(),
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = agreementChecked,
                                onCheckedChange = { agreementChecked = it }
                            )
                            Text(
                                text = "I understand my enquiry will be used only to respond to me about Tasaaga Primary School.",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                hasAttemptedSubmit = true
                                if (isNameValid && isEmailValid && isMessageValid && agreementChecked) {
                                    showInquirySuccess = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Rounded.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send enquiry", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // 9. FOOTER
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF3E2723)), // Dark Brown
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        TasaagaLogo()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Sitabaale, Uganda  |  Day & Boarding",
                        color = Color.White.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Inclusive Education • Stronger Communities • Brighter Futures",
                        color = Color(0xFFFDD835),
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }
    }
}

@Composable
fun SupportMiniCard(
    title: String,
    desc: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Text(desc, style = MaterialTheme.typography.labelSmall, color = Color.Gray, fontSize = 9.sp, lineHeight = 12.sp)
        }
    }
}

@Composable
fun NewsCard(news: NewsItem) {
    Card(
        modifier = Modifier.width(220.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Surface(
                color = Color(0xFFFFF3E0),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = news.category.name,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFFE65100),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(news.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall, maxLines = 1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(news.content, style = MaterialTheme.typography.bodySmall, color = Color.Gray, maxLines = 2, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(news.date, style = MaterialTheme.typography.labelSmall, color = Color.LightGray, fontSize = 10.sp)
        }
    }
}

private data class ProgrammeItem(
    val title: String,
    val desc: String,
    val icon: ImageVector,
    val color: Color
)

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun HomeScreenPreview() {
    TasaagaOVCPSTheme {
        HomeScreen()
    }
}
