package com.example.tasaagaovcps.data

import com.example.tasaagaovcps.data.repository.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.MemoryCodeVerifierCache
import io.github.jan.supabase.auth.MemorySessionManager
import io.github.jan.supabase.postgrest.Postgrest

interface AppContainer {
    val schoolRepository: SchoolRepository
    val supportRepository: SupportRepository
    val volunteerRepository: VolunteerRepository
    val newsRepository: NewsRepository
    val supabaseClient: SupabaseClient
    val userRepository: UserRepository
}

class AppContainerImpl : AppContainer {
    override val supabaseClient: SupabaseClient = createSupabaseClient(
        supabaseUrl = "https://dibxccfjbntfnzhqpcuv.supabase.co",
        supabaseKey = "sb_publishable_WTcoJ9DnRot425mTmrQFfQ_UeUEm_0h"
    ) {
        install(Auth) {
            sessionManager = MemorySessionManager()
            codeVerifierCache = MemoryCodeVerifierCache()
        }
        install(Postgrest)
    }

    override val schoolRepository: SchoolRepository by lazy { SchoolRepositoryImpl(supabaseClient) }
    override val supportRepository: SupportRepository by lazy { SupportRepositoryImpl(supabaseClient) }
    override val volunteerRepository: VolunteerRepository by lazy { VolunteerRepositoryImpl(supabaseClient) }
    override val newsRepository: NewsRepository by lazy { NewsRepositoryImpl(supabaseClient) }
    override val userRepository: UserRepository by lazy { UserRepositoryImpl(supabaseClient) }
}
