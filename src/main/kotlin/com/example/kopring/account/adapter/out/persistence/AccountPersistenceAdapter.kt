package com.example.kopring.account.adapter.out.persistence

import com.example.kopring.account.application.port.out.LoadAccountPort
import com.example.kopring.account.application.port.out.UpdateAccountStatePort
import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.common.PersistenceAdapter
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.repository.findByIdOrNull
import java.time.LocalDateTime

@PersistenceAdapter
class AccountPersistenceAdapter(
    private val accountRepository: AccountRepository,
    private val activityRepository: ActivityRepository,
    private val accountMapper: AccountMapper,
) : LoadAccountPort, UpdateAccountStatePort {

    override fun loadAccount(
        accountId: AccountId,
        baselineDate: LocalDateTime
    ): Account {
        val account = accountRepository.findByIdOrNull(accountId.value)
            ?: throw EntityNotFoundException("Account with id $accountId not found")

        val activities = activityRepository.findByOwnerSince(
            ownerAccountId = accountId.value,
            since = baselineDate
        )

        val withdrawalBalance = activityRepository.getWithdrawalBalanceUntil(
            accountId = accountId.value,
            until = baselineDate
        )

        val depositBalance = activityRepository.getDepositBalanceUntil(
            accountId = accountId.value,
            until = baselineDate
        )

        return accountMapper.mapToDomainEntity(
            account = account,
            activities = activities,
            withdrawalBalance = withdrawalBalance,
            depositBalance = depositBalance
        )
    }

    override fun updateActivities(account: Account) {
        for (activity in account.activityWindow.getActivities()) {
            if (activity.id == null) {
                activityRepository.save(accountMapper.mapToJpaEntity(activity))
            }
        }
    }
}