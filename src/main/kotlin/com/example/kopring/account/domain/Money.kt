package com.example.kopring.account.domain

import java.math.BigInteger

data class Money(
    val amount: BigInteger
) {

    companion object {
        val ZERO = of(0L)

        fun of(value: Long): Money {
            return Money(BigInteger.valueOf(value))
        }

        fun add(a: Money, b: Money): Money {
            return Money(a.amount.add(b.amount))
        }

        fun subtract(a: Money, b: Money): Money {
            return Money(a.amount.subtract(b.amount))
        }
    }

    fun isPositiveOrZero(): Boolean {
        return this.amount >= BigInteger.ZERO
    }

    fun isNegative(): Boolean {
        return this.amount < BigInteger.ZERO
    }

    fun isPositive(): Boolean {
        return this.amount > BigInteger.ZERO
    }

    fun isGreaterThanOrEqualTo(money: Money): Boolean {
        return this.amount >= money.amount
    }

    fun isGreaterThan(money: Money): Boolean {
        return this.amount > money.amount
    }

    fun minus(money: Money): Money {
        return Money(this.amount.subtract(money.amount))
    }

    fun plus(money: Money): Money {
        return Money(this.amount.add(money.amount))
    }

    fun negate(): Money {
        return Money(this.amount.negate())
    }
}