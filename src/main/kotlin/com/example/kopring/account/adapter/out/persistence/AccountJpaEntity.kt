package com.example.kopring.account.adapter.out.persistence

import jakarta.persistence.*

@Entity
@Table(name = "account")
class AccountJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
)