package com.example.kopring.account.domain

import com.example.kopring.account.domain.Account.AccountId
import java.time.LocalDateTime

data class Activity(
    val id: ActivityId? = null,
    val ownerAccountId: AccountId,
    val sourceAccountId: AccountId,
    val targetAccountId: AccountId,
    val timestamp: LocalDateTime,
    val money: Money
) {
    data class ActivityId(
        val value: Long
    )
}