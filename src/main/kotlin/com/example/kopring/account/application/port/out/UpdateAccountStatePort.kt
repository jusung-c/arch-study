package com.example.kopring.account.application.port.out

import com.example.kopring.account.domain.Account

interface UpdateAccountStatePort {

    fun updateActivities(account: Account)

}