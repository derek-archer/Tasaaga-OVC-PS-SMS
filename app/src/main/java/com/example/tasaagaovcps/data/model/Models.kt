package com.example.tasaagaovcps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SchoolInfo(
    val mission: String,
    val motto: String,
    val programs: List<String>
)

@Serializable
data class DonationInfo(
    val title: String,
    val description: String,
    @SerialName("donation_methods")
    val donationMethods: List<String>,
    @SerialName("sponsorship_details")
    val sponsorshipDetails: String
)

@Serializable
data class VolunteerOpportunity(
    val id: String,
    val title: String,
    val description: String,
    val requirements: List<String>,
    val duration: String,
    val accommodation: String
)

@Serializable
data class NewsItem(
    val id: String,
    val title: String,
    val content: String,
    val category: NewsCategory,
    val date: String,
    @SerialName("image_url")
    val imageUrl: String? = null
)

@Serializable
enum class NewsCategory {
    CLINIC, COMMUNITY, SUCCESS_STORY
}

@Serializable
data class Profile(
    val id: String,
    val role: String,
    val email: String? = null,
    val name: String? = null,
    val phone: String? = null
)

@Serializable
data class Student(
    val id: Int? = null,
    @SerialName("adm_no")
    val admNo: String,
    val fname: String,
    val lname: String,
    val dob: String? = null,
    val gender: String,
    @SerialName("student_type")
    val studentType: String,
    @SerialName("class_id")
    val classId: Int? = null,
    @SerialName("guardian_name")
    val guardianName: String? = null,
    @SerialName("guardian_phone")
    val guardianPhone: String? = null,
    val status: String = "Active"
)

@Serializable
data class Parent(
    val id: Int? = null,
    val name: String,
    val phone: String,
    val email: String? = null,
    val children: List<String> = emptyList(),
    val status: String = "Active"
)

@Serializable
data class SchoolClass(
    val id: Int? = null,
    val name: String,
    val level: Int,
    val stream: String = "A",
    @SerialName("teacher_name")
    val teacherName: String? = null
)

@Serializable
data class Subject(
    val id: Int? = null,
    val code: String,
    val name: String,
    val level: String = "All",
    @SerialName("is_core")
    val isCore: Boolean = true
)

@Serializable
data class AttendanceRecord(
    val id: Int? = null,
    @SerialName("student_id")
    val studentId: Int,
    @SerialName("student_name")
    val studentName: String,
    @SerialName("adm_no")
    val admNo: String,
    @SerialName("class_id")
    val classId: Int? = null,
    val status: String,
    @SerialName("recorded_time")
    val recordedTime: String? = null,
    val note: String? = null,
    @SerialName("recorded_by")
    val recordedBy: String,
    val date: String
)

@Serializable
data class FeeStructure(
    val id: Int? = null,
    val category: String,
    @SerialName("student_type")
    val studentType: String,
    val amount: Double,
    val term: String,
    val status: String = "Active"
)

@Serializable
data class PaymentRecord(
    val id: Int? = null,
    @SerialName("receipt_no")
    val receiptNo: String,
    @SerialName("student_id")
    val studentId: Int? = null,
    @SerialName("student_name")
    val studentName: String,
    @SerialName("class_id")
    val classId: Int? = null,
    val amount: Double,
    @SerialName("payment_method")
    val paymentMethod: String,
    @SerialName("payment_date")
    val paymentDate: String,
    @SerialName("recorded_by")
    val recordedBy: String,
    val status: String = "Confirmed",
    @SerialName("reference_no")
    val referenceNo: String? = null
)

@Serializable
data class ExpenseRecord(
    val id: Int? = null,
    val date: String,
    val category: String,
    val description: String,
    val amount: Double,
    @SerialName("requested_by")
    val requestedBy: String,
    @SerialName("approved_by")
    val approvedBy: String? = null,
    val status: String = "Pending"
)

@Serializable
data class BoardingDorm(
    val id: Int? = null,
    val name: String,
    val gender: String,
    val capacity: Int,
    val occupied: Int
)

@Serializable
data class BoardingAllocation(
    val id: Int? = null,
    @SerialName("student_id")
    val studentId: Int,
    @SerialName("student_name")
    val studentName: String,
    @SerialName("class_id")
    val classId: Int? = null,
    @SerialName("dorm_name")
    val dormName: String,
    val room: String,
    val bed: String,
    @SerialName("allocated_from")
    val allocatedFrom: String,
    val status: String = "Active"
)

@Serializable
data class WelfareIncident(
    val id: Int? = null,
    @SerialName("student_name")
    val studentName: String,
    @SerialName("incident_type")
    val incidentType: String,
    val description: String,
    val date: String,
    @SerialName("reported_by")
    val reportedBy: String,
    val status: String = "Active"
)

@Serializable
data class StaffMember(
    val id: Int? = null,
    val name: String,
    val role: String,
    val department: String,
    val phone: String,
    val email: String? = null,
    @SerialName("joined_date")
    val joinedDate: String,
    val status: String = "Active"
)

@Serializable
data class InventoryAsset(
    val id: Int? = null,
    @SerialName("asset_code")
    val assetCode: String,
    val name: String,
    val category: String,
    val condition: String,
    val location: String,
    val custodian: String,
    @SerialName("estimated_value")
    val estimatedValue: Double
)

@Serializable
data class StockItem(
    val id: Int? = null,
    @SerialName("item_name")
    val itemName: String,
    val category: String,
    val quantity: Int,
    @SerialName("min_level")
    val minLevel: Int,
    val unit: String,
    @SerialName("low_stock_alert")
    val lowStockAlert: Boolean = false
)

@Serializable
data class StockMovement(
    val id: Int? = null,
    val date: String,
    @SerialName("item_name")
    val itemName: String,
    @SerialName("movement_type")
    val movementType: String,
    val quantity: Int,
    val reason: String,
    @SerialName("recorded_by")
    val recordedBy: String
)

@Serializable
data class AnnouncementItem(
    val id: Int? = null,
    val title: String,
    val body: String,
    val audience: String,
    @SerialName("published_by")
    val publishedBy: String,
    val date: String,
    val status: String = "Published"
)

@Serializable
data class AuditLogEntry(
    val id: Int? = null,
    val timestamp: String? = null,
    val actor: String,
    val action: String,
    val entity: String,
    val details: String,
    @SerialName("ip_address")
    val ipAddress: String? = null
)
