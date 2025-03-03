package com.example.kopring.account.adapter.out.persistence

import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.ActivityWindow
import com.example.kopring.account.domain.Money
import com.example.kopring.common.AccountTestData.defaultAccount
import com.example.kopring.common.ActivityTestData.defaultActivity
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@DataJpaTest
@Import(AccountPersistenceAdapter::class, AccountMapper::class)
class AccountPersistenceAdapterTest {

    @Autowired
    private lateinit var adapterUnderTest: AccountPersistenceAdapter

    @Autowired
    private lateinit var activityRepository: ActivityRepository

    @Test
    @Sql("/account/AccountPersistenceAdapterTest.sql")
    fun loadsAccount() {
        // when
        val account = adapterUnderTest.loadAccount(
            accountId = Account.AccountId(1L),
            baselineDate = LocalDateTime.of(2018, 8, 10, 0, 0)
        )

        // then
        account.activityWindow.getActivities().size shouldBe 2
        account.calculateBalance() shouldBe Money.of(500L)
    }

    @Test
    fun updatesActivities() {
        val account = defaultAccount().copy(
            baselineBalance = Money.of(555L),
            activityWindow = ActivityWindow(
                defaultActivity().copy(
                    id = null,
                    money = Money.of(1L)
                )
            )
        )

        // when
        adapterUnderTest.updateActivities(account)

        // then
        activityRepository.count() shouldBe 1

        val savedActivity = activityRepository.findAll().first()
        savedActivity.amount shouldBe 1L
    }
}