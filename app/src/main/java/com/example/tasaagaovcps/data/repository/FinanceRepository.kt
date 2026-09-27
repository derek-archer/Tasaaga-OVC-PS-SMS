package com.example.tasaagaovcps.data.repository

import com.example.tasaagaovcps.data.model.ExpenseRecord
import com.example.tasaagaovcps.data.model.FeeStructure
import com.example.tasaagaovcps.data.model.PaymentRecord
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order

interface FinanceRepository {
    suspend fun getRecentPayments(schoolId: String, limit: Int = 50): List<PaymentRecord>
    suspend fun getPaymentsByStudent(studentId: String): List<PaymentRecord>
    suspend fun getFeeStructures(schoolId: String): List<FeeStructure>
    suspend fun getRecentExpenses(schoolId: String, limit: Int = 50): List<ExpenseRecord>
    suspend fun getTotalCollected(schoolId: String): Double
    suspend fun getTotalExpenses(schoolId: String): Double
}

class FinanceRepositoryImpl(
    private val client: SupabaseClient
) : FinanceRepository {

    override suspend fun getRecentPayments(schoolId: String, limit: Int): List<PaymentRecord> {
        return try {
            client.postgrest["payments"]
                .select {
                    filter { eq("school_id", schoolId) }
                    order("payment_date", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<PaymentRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getPaymentsByStudent(studentId: String): List<PaymentRecord> {
        return try {
            client.postgrest["payments"]
                .select {
                    filter { eq("student_id", studentId) }
                    order("payment_date", Order.DESCENDING)
                }
                .decodeList<PaymentRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getFeeStructures(schoolId: String): List<FeeStructure> {
        return try {
            client.postgrest["fee_structures"]
                .select {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<FeeStructure>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getRecentExpenses(schoolId: String, limit: Int): List<ExpenseRecord> {
        return try {
            client.postgrest["expenses"]
                .select {
                    filter { eq("school_id", schoolId) }
                    order("date", Order.DESCENDING)
                    limit(limit.toLong())
                }
                .decodeList<ExpenseRecord>()
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getTotalCollected(schoolId: String): Double {
        return try {
            val payments = client.postgrest["payments"]
                .select {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<PaymentRecord>()
            payments.sumOf { it.amount }
        } catch (e: Exception) {
            0.0
        }
    }

    override suspend fun getTotalExpenses(schoolId: String): Double {
        return try {
            val expenses = client.postgrest["expenses"]
                .select {
                    filter { eq("school_id", schoolId) }
                }
                .decodeList<ExpenseRecord>()
            expenses.sumOf { it.amount }
        } catch (e: Exception) {
            0.0
        }
    }
}
