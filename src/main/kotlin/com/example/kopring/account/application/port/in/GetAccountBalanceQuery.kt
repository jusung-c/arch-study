package com.example.kopring.account.application.port.`in`

import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Money

interface GetAccountBalanceQuery {

    fun getAccountBalance(accountId: AccountId): Money

}