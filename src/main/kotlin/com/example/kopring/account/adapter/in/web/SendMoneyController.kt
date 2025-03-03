package com.example.kopring.account.adapter.`in`.web

import com.example.kopring.account.application.port.`in`.SendMoneyCommand
import com.example.kopring.account.application.port.`in`.SendMoneyUseCase
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Money
import com.example.kopring.common.WebAdapter
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@WebAdapter
@RestController
class SendMoneyController(
    private val sendMoneyUseCase: SendMoneyUseCase,
) {

    @PostMapping(path = ["/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}"])
    fun sendMoney(
        @PathVariable sourceAccountId: Long,
        @PathVariable targetAccountId: Long,
        @PathVariable amount: Long,
    ) {
        val command = SendMoneyCommand(
            sourceAccountId = AccountId(sourceAccountId),
            targetAccountId = AccountId(targetAccountId),
            money = Money.of(amount)
        )

        sendMoneyUseCase.sendMoney(command)
    }
}