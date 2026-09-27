package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.BoardingAllocation
import com.example.tasaagaovcps.data.model.BoardingDorm
import com.example.tasaagaovcps.data.model.WelfareIncident
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

interface BoardingRepository {
    suspend fun getDorms(schoolId: String): List<BoardingDorm>
    suspend fun getAllocations(schoolId: String): List<BoardingAllocation>
    suspend fun getWelfareIncidents(schoolId: String, limit: Int = 20): List<WelfareIncident>
    suspend fun insertWelfareIncident(incident: WelfareIncident)
}

class BoardingRepositoryImpl(
    private val client: SupabaseClient
) : BoardingRepository {

    override suspend fun getDorms(schoolId: String): List<BoardingDorm> {
        return try {
            // Live DB table is "dormitories" (boarding_dorms is in local schema but not deployed)
            client.postgrest["dormitories"]
                .select {
                    filter { eq("school_id", schoolId) }
                    order("name", Order.ASCENDING)
                }
                .decodeList<BoardingDorm>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getAllocations(schoolId: String): List<BoardingAllocation> {
        return try {
            client.postgrest["boarding_allocations"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        eq("status", "Active")
                    }
                }
                .decodeList<BoardingAllocation>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getWelfareIncidents(schoolId: String, limit: Int): List<WelfareIncident> {
        return try {
            client.postgrest["boarding_welfare"]
                .select {
                    filter { eq("school_id", schoolId) }
                    order("date", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<WelfareIncident>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun insertWelfareIncident(incident: WelfareIncident) {
        try {
            client.postgrest["boarding_welfare"].insert(incident)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
