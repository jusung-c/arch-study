package com.example.kopring.account.application.port.`in`

interface SendMoneyUseCase {

    fun sendMoney(command: SendMoneyCommand): Boolean

}