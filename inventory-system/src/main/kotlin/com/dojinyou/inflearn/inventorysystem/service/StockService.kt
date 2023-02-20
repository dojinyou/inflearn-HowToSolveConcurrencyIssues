package com.dojinyou.inflearn.inventorysystem.service

import com.dojinyou.inflearn.inventorysystem.repository.StockRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class StockService(
    private val stockRepository: StockRepository
) {
    @Transactional
    fun decrease(stockId: UUID, quantity: Long) {
        // get stock
        val stock = stockRepository.findById(stockId).orElseThrow()

        // 재고 감소
        stock.decrease(quantity)

        // 저장
        stockRepository.saveAndFlush(stock)
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun decreaseWithNewTransaction(stockId: UUID, quantity: Long) {
        // get stock
        val stock = stockRepository.findById(stockId).orElseThrow()

        // 재고 감소
        stock.decrease(quantity)

        // 저장
        stockRepository.saveAndFlush(stock)
    }

    @Transactional
    fun decreaseWithPessimisticLock(stockId: UUID, quantity: Long) {
        // get stock
        val stock = stockRepository.findByIdWithPessimisticLock(stockId)

        // 재고 감소
        stock.decrease(quantity)

        // 저장
        stockRepository.saveAndFlush(stock)
    }

    @Transactional
    fun decreaseWithOptimisticLock(stockId: UUID, quantity: Long) {
        // get stock
        val stock = stockRepository.findByIdWithOptimisticLock(stockId)

        // 재고 감소
        stock.decrease(quantity)

        // 저장
        stockRepository.saveAndFlush(stock)
    }
}
