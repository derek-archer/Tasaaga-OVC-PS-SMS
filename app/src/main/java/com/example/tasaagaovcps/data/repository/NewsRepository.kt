package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.NewsCategory
import com.example.tasaagaovcps.data.model.NewsItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface NewsRepository {
    fun getNewsItems(): Flow<List<NewsItem>>
}

class NewsRepositoryImpl : NewsRepository {
    override fun getNewsItems(): Flow<List<NewsItem>> = flowOf(
        listOf(
            NewsItem(
                id = "1",
                title = "Musawo Clinic Update",
                content = "The Musawo clinic has successfully treated over 100 community members this month.",
                category = NewsCategory.CLINIC,
                date = "2026-09-15",
                imageUrl = "https://images.unsplash.com/photo-1584515979956-d9f6e5d09982?auto=format&fit=crop&q=80&w=600"
            ),
            NewsItem(
                id = "2",
                title = "Success Story: Sarah's Journey",
                content = "Sarah, a former OVC student, has graduated from vocational training and started her own business.",
                category = NewsCategory.SUCCESS_STORY,
                date = "2026-09-10",
                imageUrl = "https://images.unsplash.com/photo-1544717305-2782549b5136?auto=format&fit=crop&q=80&w=600"
            ),
            NewsItem(
                id = "3",
                title = "Community Outreach Program",
                content = "Our team visited local villages to provide health education and support.",
                category = NewsCategory.COMMUNITY,
                date = "2026-09-05",
                imageUrl = "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?auto=format&fit=crop&q=80&w=600"
            )
        )
    )
}
