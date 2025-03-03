package com.example.kopring.account.application.domain

import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.ActivityWindow
import com.example.kopring.account.domain.Money
import com.example.kopring.common.ActivityTestData.defaultActivity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.time.LocalDateTime


class ActivityWindowTest {

    @Test
    fun calculatesStartTimestamp() {
        val window = ActivityWindow(
            defaultActivity().copy(timestamp = startDate()),
            defaultActivity().copy(timestamp = inBetweenDate()),
            defaultActivity().copy(timestamp = endDate())
        )

        window.startTimestamp shouldBe startDate()
    }

    @Test
    fun calculatesEndTimestamp() {
        val window = ActivityWindow(
            defaultActivity().copy(timestamp = startDate()),
            defaultActivity().copy(timestamp = inBetweenDate()),
            defaultActivity().copy(timestamp = endDate())
        )

        window.endTimestamp shouldBe endDate()
    }

    @Test
    fun calculateBalance() {
        val account1 = AccountId(1L)
        val account2 = AccountId(2L)

        val window = ActivityWindow(
            defaultActivity().copy(
                sourceAccountId = account1,
                targetAccountId = account2,
                money = Money.of(999L)
            ),
            defaultActivity().copy(
                sourceAccountId = account1,
                targetAccountId = account2,
                money = Money.of(1L)
            ),
            defaultActivity().copy(
                sourceAccountId = account2,
                targetAccountId = account1,
                money = Money.of(500L)
            ),
        )

        window.calculateBalance(account1) shouldBe Money.of(-500L)
        window.calculateBalance(account2) shouldBe Money.of(500L)
    }


    private fun startDate(): LocalDateTime {
        return LocalDateTime.of(2024, 8, 3, 0, 0)
    }

    private fun inBetweenDate(): LocalDateTime {
        return LocalDateTime.of(2024, 8, 4, 0, 0)
    }

    private fun endDate(): LocalDateTime {
        return LocalDateTime.of(2024, 8, 5, 0, 0)
    }

}