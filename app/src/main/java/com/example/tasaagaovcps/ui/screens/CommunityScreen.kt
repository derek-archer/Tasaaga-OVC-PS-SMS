package com.example.tasaagaovcps.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.HealthAndSafety
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun CommunityContent(
    newsItems: List<NewsItem>,
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
                text = "Community & Health News",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Bold
            )
        }

        items(newsItems) { item ->
            val contentColor = when (item.category) {
                NewsCategory.CLINIC -> MaterialTheme.colorScheme.onPrimaryContainer
                NewsCategory.SUCCESS_STORY -> MaterialTheme.colorScheme.onTertiaryContainer
                NewsCategory.COMMUNITY -> MaterialTheme.colorScheme.onSurfaceVariant
            }
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when (item.category) {
                        NewsCategory.CLINIC -> MaterialTheme.colorScheme.primaryContainer
                        NewsCategory.SUCCESS_STORY -> MaterialTheme.colorScheme.tertiaryContainer
                        NewsCategory.COMMUNITY -> MaterialTheme.colorScheme.surfaceVariant
                    }
                )
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
                            .height(150.dp)
                            .padding(bottom = 12.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = when (item.category) {
                                NewsCategory.CLINIC -> Icons.Rounded.HealthAndSafety
                                NewsCategory.SUCCESS_STORY -> Icons.Rounded.Star
                                NewsCategory.COMMUNITY -> Icons.Rounded.Star
                            },
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = contentColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.category.name,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = contentColor
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = contentColor
                    )
                    Text(
                        text = item.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = contentColor.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.content,
                        style = MaterialTheme.typography.bodyMedium,
                        color = contentColor
                    )
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
                    date = "Oct 20, 2026"
                ),
                NewsItem(
                    id = "2",
                    title = "Success Story: John's Journey",
                    content = "John graduated from our vocational program and is now a successful mechanic in town.",
                    category = NewsCategory.SUCCESS_STORY,
                    date = "Oct 15, 2026"
                )
            )
        )
    }
}
