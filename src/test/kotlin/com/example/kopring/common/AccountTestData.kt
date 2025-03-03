package com.example.kopring.common

import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.ActivityWindow
import com.example.kopring.account.domain.Money

object AccountTestData {

    fun defaultAccount(): Account {
        return Account(
            id = AccountId(42L),
            baselineBalance = Money.of(999L),
            activityWindow = ActivityWindow(
                listOf(
                    ActivityTestData.defaultActivity(),
                    ActivityTestData.defaultActivity()
                )
            )
        )
    }
}