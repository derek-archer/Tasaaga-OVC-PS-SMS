package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.DonationInfo
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface SupportRepository {
    fun getDonationInfo(): Flow<DonationInfo>
}

class SupportRepositoryImpl(private val supabaseClient: SupabaseClient) : SupportRepository {
    private val defaultDonationInfo = DonationInfo(
        title = "Support Tasaaga OVC PS",
        description = "Your support helps us provide quality education and care to vulnerable children.",
        donationMethods = listOf("Bank Transfer", "Mobile Money", "Online Credit Card"),
        sponsorshipDetails = "Sponsorship for OVC (Orphans and Vulnerable Children) includes subsidized fees and essential school supplies."
    )

    override fun getDonationInfo(): Flow<DonationInfo> = flow {
        val remoteInfo = try {
            supabaseClient.postgrest.from("donation_info").select().decodeSingleOrNull<DonationInfo>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
        emit(remoteInfo ?: defaultDonationInfo)
    }
}
