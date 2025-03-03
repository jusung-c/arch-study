package com.example.kopring.account.application.domain

import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.ActivityWindow
import com.example.kopring.account.domain.Money
import com.example.kopring.common.AccountTestData.defaultAccount
import com.example.kopring.common.ActivityTestData.defaultActivity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class AccountTest {

    @Test
    fun calculateBalance() {
        val accountId = AccountId(1L)
        val account = defaultAccount().copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(999L)
                ),
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(1L)
                )
            )
        )
        val balance = account.calculateBalance()

        balance shouldBe Money.of(1555L)
    }

    @Test
    fun withdrawalSucceeds() {
        val accountId = AccountId(1L)
        val account = defaultAccount().copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(999L)
                ),
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(1L)
                )
            )
        )

        val success = account.withdraw(
            money = Money.of(555L),
            targetAccountId = AccountId(99L)
        )

        success shouldBe true
        account.activityWindow.getActivities().size shouldBe 3
        account.calculateBalance() shouldBe Money.of(1000L)
    }

    @Test
    fun withdrawalFailure() {
        val accountId = AccountId(1L)
        val account = defaultAccount().copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(999L)
                ),
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(1L)
                )
            )
        )

        val success = account.withdraw(
            money = Money.of(1556),
            targetAccountId = AccountId(99L)
        )

        success shouldBe false
        account.activityWindow.getActivities().size shouldBe 2
        account.calculateBalance() shouldBe Money.of(1555L)
    }

    @Test
    fun depositSuccess() {
        val accountId = AccountId(1L)
        val account = defaultAccount().copy(
            id = accountId,
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(999L)
                ),
                defaultActivity().copy(
                    targetAccountId = accountId,
                    money = Money.of(1L)
                )
            )
        )

        val success = account.deposit(
            money = Money.of(445L),
            sourceAccountId = AccountId(99L)
        )

        success shouldBe true
        account.activityWindow.getActivities().size shouldBe 3
        account.calculateBalance() shouldBe Money.of(2000L)
    }

}