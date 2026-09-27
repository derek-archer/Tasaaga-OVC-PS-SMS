package com.example.tasaagaovcps.data

import com.example.tasaagaovcps.BuildConfig
import com.example.tasaagaovcps.data.repository.*
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.auth.MemoryCodeVerifierCache
import io.github.jan.supabase.auth.MemorySessionManager
import io.github.jan.supabase.postgrest.Postgrest

interface AppContainer {
    val supabaseClient: SupabaseClient
    val schoolRepository: SchoolRepository
    val supportRepository: SupportRepository
    val volunteerRepository: VolunteerRepository
    val newsRepository: NewsRepository
    val userRepository: UserRepository
    val studentRepository: StudentRepository
    val attendanceRepository: AttendanceRepository
    val financeRepository: FinanceRepository
    val examResultRepository: ExamResultRepository
    val announcementRepository: AnnouncementRepository
    val boardingRepository: BoardingRepository
    val staffRepository: StaffRepository
    val geminiRepository: GeminiRepository
    val contactRepository: ContactRepository
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
    override val studentRepository: StudentRepository by lazy { StudentRepositoryImpl(supabaseClient) }
    override val attendanceRepository: AttendanceRepository by lazy { AttendanceRepositoryImpl(supabaseClient) }
    override val financeRepository: FinanceRepository by lazy { FinanceRepositoryImpl(supabaseClient) }
    override val examResultRepository: ExamResultRepository by lazy { ExamResultRepositoryImpl(supabaseClient) }
    override val announcementRepository: AnnouncementRepository by lazy { AnnouncementRepositoryImpl(supabaseClient) }
    override val boardingRepository: BoardingRepository by lazy { BoardingRepositoryImpl(supabaseClient) }
    override val staffRepository: StaffRepository by lazy { StaffRepositoryImpl(supabaseClient) }
    override val geminiRepository: GeminiRepository by lazy { GeminiRepositoryImpl(BuildConfig.GEMINI_API_KEY) }
    override val contactRepository: ContactRepository by lazy { ContactRepositoryImpl() }
}
