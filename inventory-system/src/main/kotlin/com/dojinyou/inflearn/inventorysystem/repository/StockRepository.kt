package com.dojinyou.inflearn.inventorysystem.repository

import com.dojinyou.inflearn.inventorysystem.domain.Stock
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface StockRepository: JpaRepository<Stock, UUID> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    fun findByIdWithPessimisticLock(id:UUID): Stock

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    fun findByIdWithOptimisticLock(id:UUID): Stock

    @Query("select get_lock(:key, 3000)", nativeQuery = true)
    fun getLock(key: String)

    @Query("select release_lock(:key)", nativeQuery = true)
    fun releaseLock(key: String)
}
