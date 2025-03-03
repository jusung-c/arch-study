package com.example.kopring.account.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface ActivityRepository : JpaRepository<ActivityJpaEntity, Long> {

    @Query(
        """
            SELECT a FROM ActivityJpaEntity as a
            WHERE a.ownerAccountId = :ownerAccountId
            AND a.timestamp >= :since
        """

    )
    fun findByOwnerSince(
        @Param("ownerAccountId") ownerAccountId: Long,
        @Param("since") since: LocalDateTime
    ): List<ActivityJpaEntity>

    @Query(
        """
            SELECT sum(a.amount) FROM ActivityJpaEntity as a
            WHERE a.targetAccountId = :accountId
            AND a.ownerAccountId = :accountId
            AND a.timestamp < :until
        """
    )
    fun getDepositBalanceUntil(
        @Param("accountId") accountId: Long,
        @Param("until") until: LocalDateTime
    ): Long

    @Query(
        """
            SELECT sum(a.amount) FROM ActivityJpaEntity as a
            WHERE a.sourceAccountId = :accountId
            AND a.ownerAccountId = :accountId
            AND a.timestamp < :until
        """
    )
    fun getWithdrawalBalanceUntil(
        @Param("accountId") accountId: Long,
        @Param("until") until: LocalDateTime
    ): Long

}