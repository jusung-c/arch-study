package com.example.kopring.account.application.service

import com.example.kopring.account.application.port.`in`.SendMoneyCommand
import com.example.kopring.account.application.port.out.AccountLock
import com.example.kopring.account.application.port.out.LoadAccountPort
import com.example.kopring.account.application.port.out.UpdateAccountStatePort
import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Money
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class SendMoneyServiceTest {

    private lateinit var loadAccountPort: LoadAccountPort
    private lateinit var updateAccountStatePort: UpdateAccountStatePort
    private lateinit var accountLock: AccountLock
    private lateinit var sendMoneyService: SendMoneyService

    @BeforeEach
    fun setup() {
        loadAccountPort = mockk()
        updateAccountStatePort = mockk {
            every { updateActivities(any()) } returns Unit
        }
        accountLock = mockk {
            every { lockAccount(any()) } returns Unit
            every { releaseAccount(any()) } returns Unit
        }
        sendMoneyService = SendMoneyService(
            loadAccountPort,
            accountLock,
            updateAccountStatePort,
            moneyTransferProperties()
        )
    }

    @Test
    fun givenWithdrawalFails_thenOnlySourceAccountIsLockedAndReleased() {
        // given
        val sourceAccountId = AccountId(41L)
        val sourceAccount = givenAnAccountWithId(sourceAccountId)

        val targetAccountId = AccountId(42L)
        val targetAccount = givenAnAccountWithId(targetAccountId)

        givenWithdrawalWillFail(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val command = SendMoneyCommand(
            sourceAccountId = sourceAccountId,
            targetAccountId = targetAccountId,
            money = Money.of(100L)
        )

        // when
        val success = sendMoneyService.sendMoney(command)

        // then
        success shouldBe false

        verify {
            accountLock.lockAccount(eq(sourceAccountId))
            accountLock.releaseAccount(eq(sourceAccountId))
        }
        verify(exactly = 0) {
            accountLock.lockAccount(eq(targetAccountId))
        }

    }

    @Test
    fun transactionSucceeds() {
        // given
        val sourceAccount = givenSourceAccount()
        val targetAccount = givenTargetAccount()
        val sourceAccountId = sourceAccount.id!!
        val targetAccountId = targetAccount.id!!

        givenWithdrawalWillSucceed(sourceAccount)
        givenDepositWillSucceed(targetAccount)

        val money = Money.of(500L)
        val command = SendMoneyCommand(
            sourceAccountId = sourceAccountId,
            targetAccountId = targetAccountId,
            money = money
        )

        // when
        val success = sendMoneyService.sendMoney(command)

        // then
        success shouldBe true

        verify {
            accountLock.lockAccount(eq(sourceAccountId))
            accountLock.lockAccount(eq(targetAccountId))
            sourceAccount.withdraw(eq(money), eq(targetAccountId))
            targetAccount.deposit(eq(money), eq(sourceAccountId))
            updateAccountStatePort.updateActivities(eq(sourceAccount))
            updateAccountStatePort.updateActivities(eq(targetAccount))
            accountLock.releaseAccount(eq(sourceAccountId))
            accountLock.releaseAccount(eq(targetAccountId))
        }

        thenAccountsHaveBeenUpdated(sourceAccountId, targetAccountId);
    }

    private fun thenAccountsHaveBeenUpdated(vararg accountIds: AccountId) {
        val accountCaptor = mutableListOf<Account>()

        verify(exactly = accountIds.size) {
            updateAccountStatePort.updateActivities(capture(accountCaptor))
        }

        val updatedAccountIds = accountCaptor.map { it.id!! }

        accountIds.forEach { accountId ->
            updatedAccountIds shouldContain accountId
        }
    }

    private fun givenDepositWillSucceed(account: Account) {
        every { account.deposit(any(), any()) } returns true
    }

    private fun givenWithdrawalWillSucceed(account: Account) {
        every { account.withdraw(any(), any()) } returns true
    }

    private fun givenWithdrawalWillFail(account: Account) {
        every { account.withdraw(any(), any()) } returns false
    }

    private fun givenTargetAccount() = givenAnAccountWithId(AccountId(42L))
    private fun givenSourceAccount() = givenAnAccountWithId(AccountId(41L))

    private fun givenAnAccountWithId(id: AccountId): Account {
        val account = mockk<Account>()

        every { account.id } returns id
        every { account.deposit(any(), any()) } returns false
        every { loadAccountPort.loadAccount(eq(id), any()) } returns account

        return account
    }

    private fun moneyTransferProperties(): MoneyTransferProperties {
        return MoneyTransferProperties(Money.of(Long.MAX_VALUE))
    }

}