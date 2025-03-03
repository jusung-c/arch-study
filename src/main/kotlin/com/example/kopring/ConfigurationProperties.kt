package com.example.kopring

import org.springframework.boot.context.properties.ConfigurationProperties


//@ConfigurationProperties(prefix = "kopring")
//data class ConfigurationProperties(
//    private val transferThreshold: Long = Long.MAX_VALUE
//)

@ConfigurationProperties(prefix = "kopring")
data class ConfigurationProperties(
    val transferThreshold: Long = Long.MAX_VALUE
)
