package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.Student
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

interface StudentRepository {
    suspend fun getStudentsBySchool(schoolId: String): List<Student>
    suspend fun getStudentsByClass(classId: Int): List<Student>
    suspend fun getBoardingStudents(schoolId: String): List<Student>
    suspend fun countStudents(schoolId: String): Int
    suspend fun countStudentsByGender(schoolId: String): Map<String, Int>
    suspend fun countByType(schoolId: String): Map<String, Int>
    suspend fun insertStudent(student: Student)
    suspend fun getStudentByProfile(profileId: String): Student?
}

class StudentRepositoryImpl(
    private val client: SupabaseClient
) : StudentRepository {

    override suspend fun getStudentsBySchool(schoolId: String): List<Student> {
        return try {
            client.postgrest["students"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        eq("status", "Active")
                    }
                    order("lname", Order.ASCENDING)
                }
                .decodeList<Student>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getStudentsByClass(classId: Int): List<Student> {
        return try {
            client.postgrest["students"]
                .select {
                    filter {
                        eq("class_id", classId)
                        eq("status", "Active")
                    }
                    order("lname", Order.ASCENDING)
                }
                .decodeList<Student>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getBoardingStudents(schoolId: String): List<Student> {
        return try {
            client.postgrest["students"]
                .select {
                    filter {
                        eq("school_id", schoolId)
                        eq("student_type", "Boarding")
                        eq("status", "Active")
                    }
                }
                .decodeList<Student>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun countStudents(schoolId: String): Int =
        getStudentsBySchool(schoolId).size

    override suspend fun countStudentsByGender(schoolId: String): Map<String, Int> {
        val students = getStudentsBySchool(schoolId)
        return students.groupBy { it.gender ?: "Unknown" }.mapValues { it.value.size }
    }

    override suspend fun countByType(schoolId: String): Map<String, Int> {
        val students = getStudentsBySchool(schoolId)
        return students.groupBy { it.studentType ?: "Day" }.mapValues { it.value.size }
    }

    override suspend fun insertStudent(student: Student) {
        try {
            client.postgrest["students"].insert(student)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getStudentByProfile(profileId: String): Student? {
        return try {
            // `parent_id` on the students table is a UUID FK to profiles.id
            // used for both Parent linking and Student self-linking
            client.postgrest["students"]
                .select {
                    filter { eq("parent_id", profileId) }
                    limit(1L)
                }
                .decodeSingleOrNull<Student>()
        } catch (e: Exception) { null }
    }
}
