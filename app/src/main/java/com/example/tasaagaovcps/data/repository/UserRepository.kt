package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.Profile
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest

interface UserRepository {
    val supabaseClient: SupabaseClient
    suspend fun login(email: String, password: String): Profile?
    suspend fun getUserProfile(userId: String): Profile?
}

class UserRepositoryImpl(override val supabaseClient: SupabaseClient) : UserRepository {
    override suspend fun login(email: String, password: String): Profile? {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        val user = supabaseClient.auth.currentUserOrNull() ?: return null
        return getUserProfile(user.id)
    }

    override suspend fun getUserProfile(userId: String): Profile? {
        return try {
            supabaseClient.postgrest.from("profiles").select {
                filter {
                    eq("id", userId)
                }
            }.decodeSingleOrNull<Profile>()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
