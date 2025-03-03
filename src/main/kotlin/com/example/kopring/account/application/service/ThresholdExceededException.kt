package com.example.kopring.account.application.service

import com.example.kopring.account.domain.Money

class ThresholdExceededException(threshold: Money, actual: Money) : RuntimeException(
    String.format(
        "Maximum threshold for transferring money exceeded: tried to transfer %s but threshold is %s!",
        actual,
        threshold
    )
)