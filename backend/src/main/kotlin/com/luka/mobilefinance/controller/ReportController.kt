package com.luka.mobilefinance.controller

import com.luka.mobilefinance.dto.DepositReportResponse
import com.luka.mobilefinance.service.ReportService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/reports")
// Prima zahteve za izvestaje prijavljenog korisnika
class ReportController(private val reportService: ReportService) {

    // Vraca izvestaj uplata za prosledjeni vremenski period
    @GetMapping("/deposits")
    fun getDepositReport(
        @RequestAttribute("username") username: String,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) from: LocalDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) to: LocalDate
    ): DepositReportResponse = reportService.getDepositReport(username, from, to)
}
