package com.dojinyou.inflearn.inventorysystem.facade

import com.dojinyou.inflearn.inventorysystem.service.StockService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class OptimisticLockStockFacade(
    private val service: StockService
) {
    fun decrease(id: UUID, quantity:Long) {
        while(true) {
            try {
                service.decreaseWithOptimisticLock(id, quantity)
                break
            } catch (e: Exception) {
                Thread.sleep(50)
            }
        }
    }
}
