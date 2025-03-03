package com.example.kopring.account.application.service

import com.example.kopring.account.domain.Money

data class MoneyTransferProperties(
    val maximumTransferThreshold: Money = Money.of(1000000L)
)