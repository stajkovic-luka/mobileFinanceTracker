package com.stajkovicluka.financeapp.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.stajkovicluka.financeapp.data.api.ApiClient
import com.stajkovicluka.financeapp.data.model.DailyDepositTotal
import com.stajkovicluka.financeapp.data.model.DepositReportResponse
import com.stajkovicluka.financeapp.data.model.MonthlyDepositTotal
import com.stajkovicluka.financeapp.data.repository.ReportRepository
import com.stajkovicluka.financeapp.util.TokenManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode

// Drzi izabrani izvestaj i stanje njegovog ucitavanja.
class ReportViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = ReportRepository(ApiClient.api)
    private val tokenManager = TokenManager(application.applicationContext)

    var report by mutableStateOf<DepositReportResponse?>(null)
    var averagePerGoal by mutableStateOf(BigDecimal.ZERO)
    var dailyTotals by mutableStateOf<List<DailyDepositTotal>>(emptyList())
    var monthlyTotals by mutableStateOf<List<MonthlyDepositTotal>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun loadReport(from: String, to: String) {
        report = null
        averagePerGoal = BigDecimal.ZERO
        dailyTotals = emptyList()
        monthlyTotals = emptyList()

        if (from.isBlank() || to.isBlank()) {
            errorMessage = "Izaberite oba datuma."
            return
        }
        if (from > to) {
            errorMessage = "Pocetni datum ne moze biti posle krajnjeg datuma."
            return
        }

        val token = tokenManager.getToken()
        if (token == null) {
            errorMessage = "Nema aktivne prijave."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val loadedReport = repository.getDepositReport(token, from, to)
                val year = from.take(4)
                val yearStart = "$year-01-01"
                val yearEnd = "$year-12-31"
                val yearlyReport = if (from == yearStart && to == yearEnd) {
                    loadedReport
                } else {
                    repository.getDepositReport(token, yearStart, yearEnd)
                }

                report = loadedReport
                averagePerGoal = calculateAveragePerGoal(loadedReport)
                dailyTotals = calculateDailyTotals(loadedReport)
                monthlyTotals = calculateMonthlyTotals(yearlyReport, year)
            } catch (exception: HttpException) {
                errorMessage = if (exception.code() == 400) {
                    "Proverite izabrani period."
                } else {
                    "Izvestaj trenutno nije dostupan."
                }
            } catch (exception: IOException) {
                errorMessage = "Nije moguce povezati se sa backend-om."
            } catch (exception: Exception) {
                errorMessage = "Doslo je do neocekivane greske."
            } finally {
                isLoading = false
            }
        }
    }

    private fun calculateAveragePerGoal(report: DepositReportResponse): BigDecimal {
        val numberOfGoals = report.deposits.map { it.goalId }.distinct().size
        return if (numberOfGoals == 0) {
            BigDecimal.ZERO
        } else {
            report.totalDeposited.divide(numberOfGoals.toBigDecimal(), 2, RoundingMode.HALF_UP)
        }
    }

    private fun calculateDailyTotals(report: DepositReportResponse): List<DailyDepositTotal> {
        return report.deposits
            .groupBy { it.createdAt.substringBefore("T") }
            .toSortedMap()
            .map { (date, deposits) ->
                DailyDepositTotal(date, deposits.sumOf { it.amount })
            }
    }

    private fun calculateMonthlyTotals(
        report: DepositReportResponse,
        year: String
    ): List<MonthlyDepositTotal> {
        val totalsByMonth = report.deposits
            .groupBy { it.createdAt.substringBefore("T").take(7) }
            .mapValues { (_, deposits) -> deposits.sumOf { it.amount } }

        return (1..12).map { month ->
            val monthValue = "%02d".format(month)
            val monthKey = "$year-$monthValue"
            MonthlyDepositTotal(monthKey, totalsByMonth[monthKey] ?: BigDecimal.ZERO)
        }
    }
}
