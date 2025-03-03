package com.example.kopring.account.application.port.out

import com.example.kopring.account.domain.Account.AccountId

interface AccountLock {

    fun lockAccount(accountId: AccountId)

    fun releaseAccount(accountId: AccountId)

}