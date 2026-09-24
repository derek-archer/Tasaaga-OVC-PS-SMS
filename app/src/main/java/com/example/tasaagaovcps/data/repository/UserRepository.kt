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

    // Demo Logins map for instant role testing and fallback
    private val demoProfiles = mapOf(
        "admin@tasaagaschool.org" to Profile("demo-admin", "Admin", "admin@tasaagaschool.org", "Admin User", "+256701000000"),
        "derekmukasa@gmail.com" to Profile("admin-id", "Admin", "derekmukasa@gmail.com", "Derek Mukasa", "+256701000000"),
        "headteacher@tasaagaschool.org" to Profile("demo-ht", "Headteacher", "headteacher@tasaagaschool.org", "Mrs. Rose Nakato", "+256701000001"),
        "teacher@tasaagaschool.org" to Profile("demo-teacher", "Teacher", "teacher@tasaagaschool.org", "Ms. Sarah Amoko", "+256701000002"),
        "finance@tasaagaschool.org" to Profile("demo-finance", "Finance", "finance@tasaagaschool.org", "Mr. David Okot", "+256701000004"),
        "parent@tasaagaschool.org" to Profile("demo-parent", "Parent", "parent@tasaagaschool.org", "Mr. George Achola", "+256701234567"),
        "boarding@tasaagaschool.org" to Profile("demo-boarding", "Boarding", "boarding@tasaagaschool.org", "Ms. Ruth Akello", "+256701000008"),
        "student@tasaagaschool.org" to Profile("demo-student", "Student", "student@tasaagaschool.org", "Mary Achola", "+256701234567")
    )

    override suspend fun login(email: String, password: String): Profile? {
        val trimmedEmail = email.trim().lowercase()

        // Check for Demo Account Matching
        demoProfiles[trimmedEmail]?.let { demo ->
            return demo
        }

        return try {
            supabaseClient.auth.signInWith(Email) {
                this.email = trimmedEmail
                this.password = password
            }
            val user = supabaseClient.auth.currentUserOrNull() ?: return null
            getUserProfile(user.id)
        } catch (e: Exception) {
            e.printStackTrace()
            // Fallback for any demo match
            demoProfiles[trimmedEmail]
        }
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
