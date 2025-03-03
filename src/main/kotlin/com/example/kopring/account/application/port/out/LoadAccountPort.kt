package com.example.kopring.account.application.port.out

import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Account.AccountId
import java.time.LocalDateTime

interface LoadAccountPort {

    fun loadAccount(accountId: AccountId, baselineDate: LocalDateTime): Account

}