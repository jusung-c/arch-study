package com.example.kopring.account.application.service

import com.example.kopring.account.application.port.`in`.GetAccountBalanceQuery
import com.example.kopring.account.application.port.out.LoadAccountPort
import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Money
import java.time.LocalDateTime

class GetAccountBalanceService(
    private val loadAccountPort: LoadAccountPort
) : GetAccountBalanceQuery {
    override fun getAccountBalance(accountId: Account.AccountId): Money {
        return loadAccountPort.loadAccount(
            accountId,
            LocalDateTime.now()
        ).calculateBalance()
    }
}