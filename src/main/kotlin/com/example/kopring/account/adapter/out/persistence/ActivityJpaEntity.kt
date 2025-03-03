package com.example.kopring.account.adapter.out.persistence

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "activity")
class ActivityJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var ownerAccountId: Long,

    @Column(nullable = false)
    var sourceAccountId: Long,

    @Column(nullable = false)
    var targetAccountId: Long,

    @Column(nullable = false)
    var amount: Long,

    @Column(nullable = false)
    var timestamp: LocalDateTime,
)