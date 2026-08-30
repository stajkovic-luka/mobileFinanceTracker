package com.stajkovicluka.financeapp.util

import java.math.BigDecimal

// Uklanja nepotrebne nule iz prikaza iznosa
fun formatAmount(amount: BigDecimal): String {
    return amount.stripTrailingZeros().toPlainString()
}
