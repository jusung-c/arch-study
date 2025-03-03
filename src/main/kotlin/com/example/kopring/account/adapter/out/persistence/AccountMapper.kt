package com.example.kopring.account.adapter.out.persistence

import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Activity
import com.example.kopring.account.domain.Activity.ActivityId
import com.example.kopring.account.domain.ActivityWindow
import com.example.kopring.account.domain.Money
import org.springframework.stereotype.Component

@Component
class AccountMapper {

    fun mapToDomainEntity(
        account: AccountJpaEntity,
        activities: List<ActivityJpaEntity>,
        withdrawalBalance: Long,
        depositBalance: Long,
    ): Account {
        val baselineBalance = Money.subtract(
            Money.of(depositBalance),
            Money.of(withdrawalBalance)
        )

        return Account.withId(
            accountId = AccountId(account.id!!),
            baselineBalance = baselineBalance,
            activityWindow = mapToActivityWindow(activities)
        )
    }

    fun mapToActivityWindow(activities: List<ActivityJpaEntity>): ActivityWindow {
        val mappedActivities = mutableListOf<Activity>()

        for (activity in activities) {
            mappedActivities.add(
                Activity(
                    id = ActivityId(activity.id!!),
                    ownerAccountId = AccountId(activity.ownerAccountId),
                    sourceAccountId = AccountId(activity.sourceAccountId),
                    targetAccountId = AccountId(activity.targetAccountId),
                    timestamp = activity.timestamp,
                    money = Money.of(activity.amount)
                )
            )
        }

        return ActivityWindow(mappedActivities)
    }

    fun mapToJpaEntity(activity: Activity) : ActivityJpaEntity {
        return ActivityJpaEntity(
            id = activity.id?.value,
            ownerAccountId = activity.ownerAccountId.value,
            sourceAccountId = activity.sourceAccountId.value,
            targetAccountId = activity.targetAccountId.value,
            amount = activity.money.amount.toLong(),
            timestamp = activity.timestamp
        )
    }
}
