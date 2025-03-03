package com.example.kopring

import com.example.kopring.account.application.service.MoneyTransferProperties
import com.example.kopring.account.domain.Money
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(ConfigurationProperties::class)
class Configuration {
    @Bean
    fun moneyTransferProperties(
        configurationProperties: ConfigurationProperties
    ): MoneyTransferProperties {
        return MoneyTransferProperties(
            Money.of(configurationProperties.transferThreshold)
        )
    }
}