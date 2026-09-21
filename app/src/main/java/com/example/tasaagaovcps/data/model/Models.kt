package com.example.tasaagaovcps.data.model

import kotlinx.serialization.Serializable

data class SchoolInfo(
    val mission: String,
    val motto: String,
    val programs: List<String>
)

data class DonationInfo(
    val title: String,
    val description: String,
    val donationMethods: List<String>,
    val sponsorshipDetails: String
)

data class VolunteerOpportunity(
    val id: String,
    val title: String,
    val description: String,
    val requirements: List<String>,
    val duration: String,
    val accommodation: String
)

data class NewsItem(
    val id: String,
    val title: String,
    val content: String,
    val category: NewsCategory,
    val date: String,
    val imageUrl: String? = null
)

enum class NewsCategory {
    CLINIC, COMMUNITY, SUCCESS_STORY
}

@Serializable
data class Profile(
    val id: String,
    val role: String,
    val email: String? = null
)
