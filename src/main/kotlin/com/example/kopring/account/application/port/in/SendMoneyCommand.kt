package com.example.kopring.account.application.port.`in`

import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Money
import com.example.kopring.common.SelfValidating
import jakarta.validation.constraints.NotNull

data class SendMoneyCommand(
    @field:NotNull
    val sourceAccountId: AccountId,

    @field:NotNull
    val targetAccountId: AccountId,

    @field:NotNull
    val money: Money
) : SelfValidating<SendMoneyCommand>() {

    init {
        this.validateSelf()
    }
}