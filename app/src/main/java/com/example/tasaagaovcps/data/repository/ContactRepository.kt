package com.example.tasaagaovcps.data.repository

interface ContactRepository {
    /**
     * Sends a general enquiry email to the school via Resend.
     * The sender's [email] is placed in Reply-To so the school can reply directly.
     * Throws on network / API failure so the caller can surface the error.
     */
    suspend fun sendInquiry(
        name: String,
        email: String,
        phone: String,
        subject: String,
        message: String
    )
}

class ContactRepositoryImpl : ContactRepository {

    private val apiService: ResendApiService by lazy { buildResendService() }

    override suspend fun sendInquiry(
        name: String,
        email: String,
        phone: String,
        subject: String,
        message: String
    ) {
        val subjectLine = subject.ifBlank { "General Enquiry" }
        val phoneLine = if (phone.isNotBlank()) "<p><strong>Phone:</strong> $phone</p>" else ""

        val html = """
            <h2>New Enquiry – Tasaaga OVC Primary School</h2>
            <p><strong>Name:</strong> $name</p>
            <p><strong>Email:</strong> $email</p>
            $phoneLine
            <p><strong>Subject:</strong> $subjectLine</p>
            <hr/>
            <p>${message.replace("\n", "<br/>")}</p>
        """.trimIndent()

        val request = ResendEmailRequest(
            from = RESEND_FROM,
            to = listOf(RESEND_TO),
            subject = "Enquiry: $subjectLine – $name",
            html = html,
            reply_to = email
        )

        apiService.sendEmail(
            authorization = resendAuthHeader(),
            request = request
        )
    }
}
