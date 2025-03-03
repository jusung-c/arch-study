package com.example.kopring.common

import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Activity
import com.example.kopring.account.domain.Money
import java.time.LocalDateTime

object ActivityTestData {

    fun defaultActivity(): Activity {
        return Activity(
            ownerAccountId = AccountId(42L),
            sourceAccountId = AccountId(42L),
            targetAccountId = AccountId(41L),
            timestamp = LocalDateTime.now(),
            money = Money.of(999L)
        )
    }
}