package com.example.tasaagaovcps.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasaagaovcps.data.model.ExpenseRecord
import com.example.tasaagaovcps.data.model.PaymentRecord
import com.example.tasaagaovcps.data.repository.FinanceRepository
import com.example.tasaagaovcps.data.repository.GeminiRepository
import com.example.tasaagaovcps.ui.components.GeminiInsightState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FinanceSummary(
    val totalCollected: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val recentPayments: List<PaymentRecord> = emptyList(),
    val recentExpenses: List<ExpenseRecord> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FinanceViewModel(
    private val financeRepository: FinanceRepository,
    private val geminiRepository: GeminiRepository
) : ViewModel() {

    private val _summary = MutableStateFlow(FinanceSummary(isLoading = true))
    val summary: StateFlow<FinanceSummary> = _summary.asStateFlow()

    private val _financeInsight = MutableStateFlow<GeminiInsightState>(GeminiInsightState.Idle)
    val financeInsight: StateFlow<GeminiInsightState> = _financeInsight.asStateFlow()

    fun loadFinanceData(schoolId: String) {
        viewModelScope.launch {
            _summary.value = _summary.value.copy(isLoading = true, error = null)
            try {
                val payments  = financeRepository.getRecentPayments(schoolId, limit = 20)
                val expenses  = financeRepository.getRecentExpenses(schoolId, limit = 20)
                val collected = financeRepository.getTotalCollected(schoolId)
                val spent     = financeRepository.getTotalExpenses(schoolId)

                _summary.value = FinanceSummary(
                    totalCollected  = collected,
                    totalExpenses   = spent,
                    recentPayments  = payments,
                    recentExpenses  = expenses,
                    isLoading       = false
                )
            } catch (e: Exception) {
                _summary.value = FinanceSummary(
                    isLoading = false,
                    error = "Failed to load finance data: ${e.message}"
                )
            }
        }
    }

    fun generateFinanceInsight() {
        val data = _summary.value
        if (data.isLoading) return

        viewModelScope.launch {
            _financeInsight.value = GeminiInsightState.Loading

            val surplus = data.totalCollected - data.totalExpenses
            val topExpenses = data.recentExpenses
                .groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }
                .entries
                .sortedByDescending { it.value }
                .take(3)
                .joinToString(", ") { "${it.key}: UGX ${"%,.0f".format(it.value)}" }

            val prompt = """
                Analyse this Tasaaga School financial snapshot:
                - Total fees collected: UGX ${"%,.0f".format(data.totalCollected)}
                - Total expenses: UGX ${"%,.0f".format(data.totalExpenses)}
                - Net surplus/deficit: UGX ${"%,.0f".format(surplus)} ${if (surplus >= 0) "(surplus)" else "(DEFICIT)"}
                - Top expense categories: ${topExpenses.ifEmpty { "No data" }}
                - Recent payments received: ${data.recentPayments.size}

                Provide:
                1. Financial health assessment (healthy / watch / critical)
                2. One recommendation for the Finance Officer
                3. Flag if running a deficit
                Max 3 bullet points.
            """.trimIndent()

            val result = geminiRepository.generateInsight(prompt)
            _financeInsight.value = GeminiInsightState.Success(result)
        }
    }

    suspend fun streamAnswer(question: String, onChunk: (String) -> Unit) {
        val data = _summary.value
        val contextPrompt = """
            Context: You are assisting the Finance Officer at Tasaaga OVC Primary School.
            Total collected: UGX ${"%,.0f".format(data.totalCollected)},
            Total expenses: UGX ${"%,.0f".format(data.totalExpenses)}.

            Finance Officer question: $question
        """.trimIndent()

        geminiRepository.streamInsight(contextPrompt).collect { chunk ->
            onChunk(chunk)
        }
    }
}
