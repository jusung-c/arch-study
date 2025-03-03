package com.example.kopring.account.domain

import com.example.kopring.account.domain.Account.AccountId
import java.time.LocalDateTime

class ActivityWindow(
    activities: List<Activity>
) {

    private val activities: MutableList<Activity> = activities.toMutableList()

    constructor(vararg activities: Activity) : this(activities.toList())

    val startTimestamp: LocalDateTime
        get() = activities.minByOrNull { it.timestamp }
            ?.timestamp
            ?: throw IllegalStateException()

    val endTimestamp: LocalDateTime
        get() = activities.maxByOrNull { it.timestamp }
            ?.timestamp
            ?: throw IllegalStateException()

    fun calculateBalance(accountId: AccountId): Money {
        val depositBalance = activities
            .filter { it.targetAccountId == accountId }
            .map { it.money }
            .fold(Money.ZERO, Money.Companion::add)

        val withdrawalBalance = activities
            .filter { it.sourceAccountId == accountId }
            .map { it.money }
            .fold(Money.ZERO, Money.Companion::add)

        return Money.add(depositBalance, withdrawalBalance.negate())
    }

    fun getActivities(): List<Activity> {
        return activities.toList()
    }

    fun addActivity(activity: Activity) {
        activities.add(activity)
    }
}