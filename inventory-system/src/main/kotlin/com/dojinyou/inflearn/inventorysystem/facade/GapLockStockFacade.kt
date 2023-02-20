package com.dojinyou.inflearn.inventorysystem.facade

import com.dojinyou.inflearn.inventorysystem.repository.StockRepository
import com.dojinyou.inflearn.inventorysystem.service.StockService
import org.springframework.stereotype.Component
import java.util.*

@Component
class GapLockStockFacade(
    private val service: StockService,
    private val repository: StockRepository,
) {
    fun decrease(stockId: UUID, quantity: Long) {
        while (true) {
            try {
                repository.getLock(stockId.toString())
                service.decreaseWithNewTransaction(stockId, quantity)
            } finally {
                repository.releaseLock(stockId.toString())
            }
        }
    }
}
