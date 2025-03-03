package com.example.kopring.account.application.service

import com.example.kopring.account.application.port.`in`.SendMoneyCommand
import com.example.kopring.account.application.port.`in`.SendMoneyUseCase
import com.example.kopring.account.application.port.out.AccountLock
import com.example.kopring.account.application.port.out.LoadAccountPort
import com.example.kopring.account.application.port.out.UpdateAccountStatePort
import com.example.kopring.common.UseCase
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime


@UseCase
@Transactional
class SendMoneyService(
    private val loadAccountPort: LoadAccountPort,
    private val accountLock: AccountLock,
    private val updateAccountStatePort: UpdateAccountStatePort,
    private val moneyTransferProperties: MoneyTransferProperties,
) : SendMoneyUseCase {
    override fun sendMoney(command: SendMoneyCommand): Boolean {

        checkThreshold(command)

        val baselineDate = LocalDateTime.now().minusDays(10)

        val sourceAccount = loadAccountPort.loadAccount(
            accountId = command.sourceAccountId,
            baselineDate = baselineDate
        )
        val sourceAccountId = sourceAccount.id ?: throw IllegalStateException("Source account id should not be null")

        val targetAccount = loadAccountPort.loadAccount(
            accountId = command.targetAccountId,
            baselineDate = baselineDate
        )
        val targetAccountId = targetAccount.id ?: throw IllegalStateException("Target account id should not be null")

        accountLock.lockAccount(sourceAccountId)
        if (!sourceAccount.withdraw(
                money = command.money,
                targetAccountId = targetAccountId
            )
        ) {
            accountLock.releaseAccount(sourceAccountId)
            return false
        }

        accountLock.lockAccount(targetAccountId)
        if (!targetAccount.deposit(
                money = command.money,
                sourceAccountId = sourceAccountId
            )
        ) {
            accountLock.releaseAccount(sourceAccountId)
            accountLock.releaseAccount(targetAccountId)
            return false
        }

        updateAccountStatePort.updateActivities(sourceAccount)
        updateAccountStatePort.updateActivities(targetAccount)

        accountLock.releaseAccount(sourceAccountId)
        accountLock.releaseAccount(targetAccountId)

        return true
    }

    private fun checkThreshold(command: SendMoneyCommand) {
        if (command.money.isGreaterThan(moneyTransferProperties.maximumTransferThreshold)) {
            throw ThresholdExceededException(moneyTransferProperties.maximumTransferThreshold, command.money)
        }
    }
}