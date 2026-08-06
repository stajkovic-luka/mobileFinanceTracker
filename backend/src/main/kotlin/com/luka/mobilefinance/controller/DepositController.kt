package com.luka.mobilefinance.controller

import com.luka.mobilefinance.dto.CreateDepositRequest
import com.luka.mobilefinance.dto.DepositResponse
import com.luka.mobilefinance.service.DepositService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/goals/{goalId}/deposits")
// Prima zahteve za uplate vezane za jedan stedni cilj.
class DepositController(private val depositService: DepositService) {

    // Dodaje novu uplatu na cilj prijavljenog korisnika.
    @PostMapping
    fun createDeposit(
        @RequestAttribute("username") username: String,
        @PathVariable goalId: Long,
        @Valid @RequestBody request: CreateDepositRequest
    ): DepositResponse = depositService.createDeposit(username, goalId, request)

    // Vraca sve uplate prijavljenog korisnika za izabrani cilj.
    @GetMapping
    fun getDeposits(
        @RequestAttribute("username") username: String,
        @PathVariable goalId: Long
    ): List<DepositResponse> = depositService.getDeposits(username, goalId)
}
