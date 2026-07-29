package com.luka.mobilefinance.controller

import com.luka.mobilefinance.dto.CreateGoalRequest
import com.luka.mobilefinance.dto.GoalResponse
import com.luka.mobilefinance.service.GoalService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/goals")
// Prima HTTP zahteve vezane za stedne ciljeve prijavljenog korisnika.
class GoalController(private val goalService: GoalService) {

    // Kreira novi stedni cilj za korisnika ciji je username procitan iz JWT tokena.
    @PostMapping
    fun createGoal(
        @RequestAttribute("username") username: String,
        @Valid @RequestBody request: CreateGoalRequest
    ): GoalResponse = goalService.createGoal(username, request)

    // Vraca samo ciljeve trenutno prijavljenog korisnika.
    @GetMapping
    fun getGoals(@RequestAttribute("username") username: String): List<GoalResponse> {
        return goalService.getGoals(username)
    }
}
