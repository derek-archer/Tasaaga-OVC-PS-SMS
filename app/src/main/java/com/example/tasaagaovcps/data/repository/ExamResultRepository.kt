package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.ExamResult
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest

interface ExamResultRepository {
    suspend fun getResultsByStudent(studentId: Int, term: Int = 1, year: Int = 2026): List<ExamResult>
    suspend fun getResultsByClass(classId: Int, term: Int = 1, year: Int = 2026): List<ExamResult>
    suspend fun getSubjectAveragesByClass(classId: Int, term: Int = 1, year: Int = 2026): Map<String, Double>
    suspend fun upsertResult(result: ExamResult)
}

class ExamResultRepositoryImpl(
    private val client: SupabaseClient
) : ExamResultRepository {

    override suspend fun getResultsByStudent(studentId: Int, term: Int, year: Int): List<ExamResult> {
        return try {
            client.postgrest["exam_results"]
                .select {
                    filter {
                        eq("student_id", studentId)
                        eq("term", term)
                        eq("academic_year", year)
                    }
                }
                .decodeList<ExamResult>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getResultsByClass(classId: Int, term: Int, year: Int): List<ExamResult> {
        return try {
            client.postgrest["exam_results"]
                .select {
                    filter {
                        eq("class_id", classId)
                        eq("term", term)
                        eq("academic_year", year)
                    }
                }
                .decodeList<ExamResult>()
        } catch (e: Exception) { emptyList() }
    }

    override suspend fun getSubjectAveragesByClass(classId: Int, term: Int, year: Int): Map<String, Double> {
        val results = getResultsByClass(classId, term, year)
        return results
            .groupBy { it.subject }
            .mapValues { (_, subjectResults) ->
                if (subjectResults.isEmpty()) 0.0
                else subjectResults.sumOf { it.marks }.toDouble() / subjectResults.size
            }
    }

    override suspend fun upsertResult(result: ExamResult) {
        try {
            client.postgrest["exam_results"].upsert(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
