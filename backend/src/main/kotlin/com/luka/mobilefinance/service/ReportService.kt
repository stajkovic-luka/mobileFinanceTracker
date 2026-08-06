package com.luka.mobilefinance.service

import com.luka.mobilefinance.dto.DepositReportItem
import com.luka.mobilefinance.dto.DepositReportResponse
import com.luka.mobilefinance.entity.Deposit
import com.luka.mobilefinance.repository.DepositRepo
import com.luka.mobilefinance.repository.UserRepo
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@Service
// Sadrzi poslovnu logiku za izvestaje uplata prijavljenog korisnika.
class ReportService(
    private val depositRepo: DepositRepo,
    private val userRepo: UserRepo
) {

    // Vraca sve uplate prijavljenog korisnika izmedju dva datuma.
    @Transactional(readOnly = true)
    fun getDepositReport(username: String, from: LocalDate, to: LocalDate): DepositReportResponse {
        if (from.isAfter(to)) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "FROM date must not be after TO date")
        }

        val user = userRepo.findByUsername(username)
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists")

        val zoneId = ZoneId.systemDefault()
        val startOfPeriod = Date.from(from.atStartOfDay(zoneId).toInstant())
        val startOfNextDay = Date.from(to.plusDays(1).atStartOfDay(zoneId).toInstant())

        val deposits = depositRepo.findForUserInPeriod(
            user.id!!,
            startOfPeriod,
            startOfNextDay
        )

        return DepositReportResponse(
            from = from,
            to = to,
            totalDeposited = deposits.sumOf { it.amount },
            deposits = deposits.map(::toReportItem)
        )
    }

    // Pretvara depozit u stavku izvestaja sa podacima o cilju kojem pripada.
    private fun toReportItem(deposit: Deposit): DepositReportItem {
        return DepositReportItem(
            depositId = deposit.id!!,
            goalId = deposit.goal.id!!,
            goalName = deposit.goal.name,
            amount = deposit.amount,
            note = deposit.note,
            createdAt = deposit.createdAt!!
        )
    }
}
