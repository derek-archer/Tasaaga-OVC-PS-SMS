package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tasaagaovcps.R
import com.example.tasaagaovcps.data.model.NewsCategory
import com.example.tasaagaovcps.data.model.NewsItem
import com.example.tasaagaovcps.ui.theme.TasaagaOVCPSTheme
import com.example.tasaagaovcps.ui.viewmodel.NewsViewModel

@Composable
fun CommunityScreen(
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    val newsItems by viewModel.newsItems.collectAsState()

    CommunityContent(newsItems = newsItems, modifier = modifier)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CommunityContent(
    newsItems: List<NewsItem>,
    modifier: Modifier = Modifier
) {
    var selectedCategoryFilter by remember { mutableStateOf<NewsCategory?>(null) }
    var selectedArticleForModal by remember { mutableStateOf<NewsItem?>(null) }

    val filteredNews = if (selectedCategoryFilter == null) {
        newsItems
    } else {
        newsItems.filter { it.category == selectedCategoryFilter }
    }

    if (selectedArticleForModal != null) {
        val article = selectedArticleForModal!!
        AlertDialog(
            onDismissRequest = { selectedArticleForModal = null },
            title = {
                Text(article.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = Color(0xFFE8F5E9),
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = "${article.category.name} • ${article.date}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF1B5E20),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                    Text(article.content, style = MaterialTheme.typography.bodyLarge)
                }
            },
            confirmButton = {
                Button(
                    onClick = { selectedArticleForModal = null },
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
                        text = "COMMUNITY & IMPACT NEWS",
                        style = MaterialTheme.typography.labelLarge.copy(
                            color = Color(0xFFFDD835),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Life at Tasaaga Primary School",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            color = Color.White,
                            fontWeight = FontWeight.Black
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Discover Musawo Clinic healthcare updates, community outreach stories, and student success achievements from Sitabaale, Uganda.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                    )
                }
            }
        }

        // CATEGORY FILTER CHIPS
        item {
            Column {
                Text(
                    text = "Filter News Category",
                    style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategoryFilter == null,
                        onClick = { selectedCategoryFilter = null },
                        label = { Text("All Stories (${newsItems.size})", fontWeight = FontWeight.Bold) }
                    )
                    NewsCategory.entries.forEach { category ->
                        FilterChip(
                            selected = selectedCategoryFilter == category,
                            onClick = { selectedCategoryFilter = category },
                            label = { Text(category.name.replace("_", " "), fontWeight = FontWeight.Bold) }
                        )
                    }
                }
            }
        }

        // NEWS CARDS
        items(filteredNews) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedArticleForModal = item },
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val imageResId = when (item.id) {
                        "1" -> R.drawable.tasaaga5
                        "2" -> R.drawable.tasaaga6
                        else -> R.drawable.tasaaga7
                    }
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = item.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = when (item.category) {
                                NewsCategory.CLINIC -> Color(0xFFE8F5E9)
                                NewsCategory.SUCCESS_STORY -> Color(0xFFFFF8E1)
                                NewsCategory.COMMUNITY -> Color(0xFFE3F2FD)
                            },
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when (item.category) {
                                        NewsCategory.CLINIC -> Icons.Rounded.HealthAndSafety
                                        NewsCategory.SUCCESS_STORY -> Icons.Rounded.Star
                                        NewsCategory.COMMUNITY -> Icons.Rounded.VolunteerActivism
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = when (item.category) {
                                        NewsCategory.CLINIC -> Color(0xFF1B5E20)
                                        NewsCategory.SUCCESS_STORY -> Color(0xFFF57F17)
                                        NewsCategory.COMMUNITY -> Color(0xFF1565C0)
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = item.category.name.replace("_", " "),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = when (item.category) {
                                        NewsCategory.CLINIC -> Color(0xFF1B5E20)
                                        NewsCategory.SUCCESS_STORY -> Color(0xFFF57F17)
                                        NewsCategory.COMMUNITY -> Color(0xFF1565C0)
                                    }
                                )
                            }
                        }

                        Text(
                            text = item.date,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = item.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF424242),
                        maxLines = 3
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    TextButton(
                        onClick = { selectedArticleForModal = item },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Read full article", fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.AutoMirrored.Rounded.ArrowForward, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFB71C1C))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
fun CommunityScreenPreview() {
    TasaagaOVCPSTheme {
        CommunityContent(
            newsItems = listOf(
                NewsItem(
                    id = "1",
                    title = "Musawo Clinic Update",
                    content = "We have received new medical supplies to better serve our students and the surrounding community.",
                    category = NewsCategory.CLINIC,
                    date = "2026-09-15"
                ),
                NewsItem(
                    id = "2",
                    title = "Success Story: Sarah's Journey",
                    content = "Sarah graduated from our vocational program and is now a successful entrepreneur in town.",
                    category = NewsCategory.SUCCESS_STORY,
                    date = "2026-09-10"
                )
            )
        )
    }
}
