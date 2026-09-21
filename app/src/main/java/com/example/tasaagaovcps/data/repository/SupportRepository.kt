package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.DonationInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface SupportRepository {
    fun getDonationInfo(): Flow<DonationInfo>
}

class SupportRepositoryImpl : SupportRepository {
    override fun getDonationInfo(): Flow<DonationInfo> = flowOf(
        DonationInfo(
            title = "Support Tasaaga OVC PS",
            description = "Your support helps us provide quality education and care to vulnerable children.",
            donationMethods = listOf("Bank Transfer", "Mobile Money", "Online Credit Card"),
            sponsorshipDetails = "Sponsorship for OVC (Orphans and Vulnerable Children) includes subsidized fees and essential school supplies."
        )
    )
}
