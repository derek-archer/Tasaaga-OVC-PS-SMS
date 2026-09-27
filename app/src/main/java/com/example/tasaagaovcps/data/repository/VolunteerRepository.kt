package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.BuildConfig
import com.example.tasaagaovcps.data.model.VolunteerOpportunity
import com.squareup.moshi.Moshi
import com.squareup.moshi.JsonClass
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

@JsonClass(generateAdapter = true)
data class ResendEmailRequest(
    val from: String,
    val to: List<String>,
    val subject: String,
    val html: String,
    val reply_to: String? = null
)

interface ResendApiService {
    @POST("emails")
    suspend fun sendEmail(
        @Header("Authorization") authorization: String,
        @Body request: ResendEmailRequest
    )
}

/** Shared Resend client factory used by all email-sending repositories. */
internal fun buildResendService(): ResendApiService {
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    return Retrofit.Builder()
        .baseUrl("https://api.resend.com/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()
        .create(ResendApiService::class.java)
}

internal fun resendAuthHeader() = "Bearer ${BuildConfig.RESEND_API_KEY}"

const val RESEND_FROM = "info@derekarcher.org"
const val RESEND_TO   = "info@derekarcher.org"

interface VolunteerRepository {
    fun getOpportunities(): Flow<List<VolunteerOpportunity>>
    suspend fun sendApplication(opportunityTitle: String, name: String, email: String, message: String)
}

class VolunteerRepositoryImpl(private val supabaseClient: SupabaseClient) : VolunteerRepository {
    private val defaultOpportunities = listOf(
        VolunteerOpportunity(
            id = "1",
            title = "Mentorship Program",
            description = "Mentor students in various subjects and life skills.",
            requirements = listOf("3-6 month commitment", "Passion for education", "Background check"),
            duration = "3-6 months",
            accommodation = "On-site accommodation provided"
        ),
        VolunteerOpportunity(
            id = "2",
            title = "Vocational Trainer",
            description = "Teach vocational skills like carpentry, tailoring, or IT.",
            requirements = listOf("Expertise in a craft", "Ability to teach", "2 months minimum"),
            duration = "2+ months",
            accommodation = "On-site accommodation provided"
        )
    )

    private val apiService: ResendApiService by lazy { buildResendService() }

    override fun getOpportunities(): Flow<List<VolunteerOpportunity>> = flow {
        val remoteOpps = try {
            supabaseClient.postgrest.from("volunteer_opportunities").select().decodeList<VolunteerOpportunity>()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
        if (remoteOpps.isNotEmpty()) {
            emit(remoteOpps)
        } else {
            emit(defaultOpportunities)
        }
    }

    override suspend fun sendApplication(opportunityTitle: String, name: String, email: String, message: String) {
        val htmlContent = "<p><strong>Opportunity:</strong> $opportunityTitle</p><p><strong>Name:</strong> $name</p><p><strong>Email:</strong> $email</p><p><strong>Message:</strong> $message</p>"

        val request = ResendEmailRequest(
            from = RESEND_FROM,
            to = listOf(RESEND_TO),
            subject = "New Volunteer Application: $opportunityTitle",
            html = htmlContent,
            reply_to = email
        )

        apiService.sendEmail(
            authorization = resendAuthHeader(),
            request = request
        )
    }
}
