package com.dojinyou.inflearn.inventorysystem.facade

import com.dojinyou.inflearn.inventorysystem.repository.RedisRepository
import com.dojinyou.inflearn.inventorysystem.service.StockService
import org.springframework.stereotype.Component
import java.util.*

@Component
class LettuceLockStockFacade(
    private val stockService: StockService,
    private val redisRepository: RedisRepository,
) {

    fun decrease(key: UUID, quantity:Long) {
        while(redisRepository.lock(key.toString()).not()) {
            Thread.sleep(100)
        }

        try {
            stockService.decrease(key, quantity)
        } finally {
            redisRepository.unLock(key.toString())
        }
    }
}
