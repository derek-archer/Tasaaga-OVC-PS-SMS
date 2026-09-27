package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.AnnouncementItem
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

interface AnnouncementRepository {
    suspend fun getAnnouncements(schoolId: String, audience: String? = null): List<AnnouncementItem>
    suspend fun getPublishedAnnouncements(schoolId: String): List<AnnouncementItem>
}

class AnnouncementRepositoryImpl(
    private val client: SupabaseClient
) : AnnouncementRepository {

    override suspend fun getAnnouncements(schoolId: String, audience: String?): List<AnnouncementItem> {
        return try {
            client.postgrest["announcements"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        if (audience != null) {
                            or {
                                eq("audience", audience)
                                eq("audience", "All")
                            }
                        }
                    }
                    order("date", Order.DESCENDING)
                    limit(20L)
                }
                .decodeList<AnnouncementItem>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getPublishedAnnouncements(schoolId: String): List<AnnouncementItem> {
        return try {
            client.postgrest["announcements"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        eq("status", "Published")
                    }
                    order("date", Order.DESCENDING)
                    limit(10L)
                }
                .decodeList<AnnouncementItem>()
        } catch (e: Exception) { emptyList() }
    }
}
