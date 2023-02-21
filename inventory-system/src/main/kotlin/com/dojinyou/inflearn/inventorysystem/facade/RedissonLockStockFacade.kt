package com.dojinyou.inflearn.inventorysystem.facade

import com.dojinyou.inflearn.inventorysystem.service.StockService
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class RedissonLockStockFacade(
    private val stockService: StockService,
    private val redissonClient: RedissonClient,
) {

    fun decrease(key: UUID, quantity: Long) {
        val lock = redissonClient.getLock(key.toString())

        try {
            val available = lock.tryLock(5, 1, TimeUnit.SECONDS)

            if (available.not()) {
                println("lock 획득 실패")
                return
            }

            stockService.decrease(key, quantity)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        } finally {
            lock.unlock()
        }
    }
}
