package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.StaffMember
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

interface StaffRepository {
    suspend fun getStaff(schoolId: String): List<StaffMember>
    suspend fun countActiveStaff(schoolId: String): Int
    suspend fun countOnLeave(schoolId: String): Int
}

class StaffRepositoryImpl(
    private val client: SupabaseClient
) : StaffRepository {

    override suspend fun getStaff(schoolId: String): List<StaffMember> {
        return try {
            client.postgrest["staff"]
                .select {
                    filter { eq("school_id", schoolId) }
                    order("name", Order.ASCENDING)
                }
                .decodeList<StaffMember>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun countActiveStaff(schoolId: String): Int {
        return try {
            client.postgrest["staff"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        eq("status", "Active")
                    }
                }
                .decodeList<StaffMember>()
                .size
        } catch (e: Exception) { 0 }
    }

    override suspend fun countOnLeave(schoolId: String): Int {
        return try {
            client.postgrest["staff"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        eq("status", "On Leave")
                    }
                }
                .decodeList<StaffMember>()
                .size
        } catch (e: Exception) { 0 }
    }
}
