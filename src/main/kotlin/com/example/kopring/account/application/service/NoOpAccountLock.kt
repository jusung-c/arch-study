package com.example.kopring.account.application.service

import com.example.kopring.account.application.port.out.AccountLock
import com.example.kopring.account.domain.Account.AccountId
import org.springframework.stereotype.Component

@Component
class NoOpAccountLock : AccountLock {
    override fun lockAccount(accountId: AccountId) {
        // do nothing
    }

    override fun releaseAccount(accountId: AccountId) {
        // do nothing
    }

}