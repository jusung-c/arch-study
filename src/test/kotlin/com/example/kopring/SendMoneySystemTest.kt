package com.example.kopring

import com.example.kopring.account.application.port.out.LoadAccountPort
import com.example.kopring.account.domain.Account
import com.example.kopring.account.domain.Account.AccountId
import com.example.kopring.account.domain.Money
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.*
import org.springframework.test.context.jdbc.Sql
import java.time.LocalDateTime

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SendMoneySystemTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var loadAccountPort: LoadAccountPort

    @Test
    @Sql("/SendMoneySystemTest.sql")
    fun sendMoney() {
        val initialSourceBalance = sourceAccount().calculateBalance()
        val initialTargetBalance = targetAccount().calculateBalance()

        val response = whenSendMoney(
            sourceAccountId = sourceAccountId(),
            targetAccountId = targetAccountId(),
            amount = transferredAmount()
        )

        response.statusCode shouldBe HttpStatus.OK
        sourceAccount().calculateBalance() shouldBe initialSourceBalance.minus(transferredAmount())
        targetAccount().calculateBalance() shouldBe initialTargetBalance.plus(transferredAmount())
    }

    private fun whenSendMoney(
        sourceAccountId: AccountId,
        targetAccountId: AccountId,
        amount: Money
    ): ResponseEntity<Any> {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_JSON
        }
        val request = HttpEntity<Void>(null, headers)

        val url = "/accounts/send/{sourceAccountId}/{targetAccountId}/{amount}"
        return testRestTemplate.exchange(
            url,
            HttpMethod.POST,
            request,
            Any::class.java,
            sourceAccountId.value,
            targetAccountId.value,
            amount.amount
        )
    }

    private fun loadAccount(accountId: AccountId): Account {
        return loadAccountPort.loadAccount(
            accountId,
            LocalDateTime.now()
        )
    }

    private fun transferredAmount(): Money {
        return Money.of(500L)
    }

    private fun sourceAccount(): Account {
        return loadAccount(sourceAccountId())
    }

    private fun targetAccount(): Account {
        return loadAccount(targetAccountId())
    }

    private fun sourceAccountId(): AccountId {
        return AccountId(1L)
    }

    private fun targetAccountId(): AccountId {
        return AccountId(2L)
    }
}