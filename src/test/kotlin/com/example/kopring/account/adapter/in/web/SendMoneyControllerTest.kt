package com.example.kopring.account.adapter.`in`.web

import com.example.kopring.account.application.port.`in`.SendMoneyCommand
import com.example.kopring.account.application.port.`in`.SendMoneyUseCase
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Money
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(controllers = [SendMoneyController::class])
class SendMoneyControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var sendMoneyUseCase: SendMoneyUseCase

    @Test
    fun testSendMoney() {
        // given
        val sourceAccountId = 41L
        val targetAccountId = 42L
        val amount = 500L

        every { sendMoneyUseCase.sendMoney(any()) } returns true

        // when
        mockMvc.perform(
            post(
                "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}",
                sourceAccountId, targetAccountId, amount
            )
                .header("Content-Type", "application/json")
        )
            .andExpect(status().isOk)

        // then
        verify {
            sendMoneyUseCase.sendMoney(
                SendMoneyCommand(
                    sourceAccountId = AccountId(sourceAccountId),
                    targetAccountId = AccountId(targetAccountId),
                    money = Money.of(amount)
                )
            )
        }
    }
}