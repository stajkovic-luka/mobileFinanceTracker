package com.luka.mobilefinance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FinanceAppApplication

fun main(args: Array<String>) {
    runApplication<FinanceAppApplication>(*args)
}
