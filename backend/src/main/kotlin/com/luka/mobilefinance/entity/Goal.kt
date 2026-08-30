package com.luka.mobilefinance.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate
import java.util.Date

@Entity
@Table(name = "goals")
// Predstavlja jedan stedni cilj jednog korisnika
class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    lateinit var user: User

    @Column(name = "name")
    lateinit var name: String

    @Column(name = "target_amount")
    lateinit var targetAmount: BigDecimal

    @Column(name = "current_amount")
    lateinit var currentAmount: BigDecimal

    // Rok nije obavezan prilikom kreiranja cilja
    @Column(name = "deadline")
    var deadline: LocalDate? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    lateinit var status: Status

    @Column(name = "created_at")
    var createdAt: Date? = null

    @Column(name = "updated_at")
    var updatedAt: Date? = null
}
