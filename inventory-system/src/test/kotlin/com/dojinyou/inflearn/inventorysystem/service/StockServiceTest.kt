package com.dojinyou.inflearn.inventorysystem.service

import com.dojinyou.inflearn.inventorysystem.domain.Stock
import com.dojinyou.inflearn.inventorysystem.facade.GapLockStockFacade
import com.dojinyou.inflearn.inventorysystem.facade.LettuceLockStockFacade
import com.dojinyou.inflearn.inventorysystem.facade.OptimisticLockStockFacade
import com.dojinyou.inflearn.inventorysystem.facade.RedissonLockStockFacade
import com.dojinyou.inflearn.inventorysystem.repository.StockRepository
import com.github.f4b6a3.ulid.UlidCreator
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

@SpringBootTest
class StockServiceTest {

    @Autowired
    private lateinit var stockService: StockService

    @Autowired
    private lateinit var pacade: OptimisticLockStockFacade

    @Autowired
    private lateinit var namedLockFacade: GapLockStockFacade

    @Autowired
    private lateinit var lettuceLockStockFacade: LettuceLockStockFacade

    @Autowired
    private lateinit var redissonLockStockFacade: RedissonLockStockFacade

    @Autowired
    private lateinit var stockRepository: StockRepository

    private lateinit var stockId: UUID
    private val baseQuantity = 100L



    @BeforeEach
    fun before() {
        val productId = UlidCreator.getMonotonicUlid().toUuid()
        val stock = Stock(productId, baseQuantity)
        stockId = stock.id
        stockRepository.saveAndFlush(stock)
    }

    @AfterEach
    fun after() {
        stockRepository.deleteAll()
    }

    @Test
    fun stockDecrease() {
        val decreaseQuantity = 1L
        stockService.decrease(stockId, decreaseQuantity)

        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - decreaseQuantity)
    }

    @Test
    fun sameTime100Request() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val quantity = 1L

        for (i in 1..threadCount)  {
            executorService.submit {
                try {
                    stockService.decrease(stockId, quantity)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()



        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - (quantity * threadCount))
    }

    @Test
    fun sameTime100RequestWithPessimisticLock() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val quantity = 1L

        for (i in 1..threadCount)  {
            executorService.submit {
                try {
                    stockService.decreaseWithPessimisticLock(stockId, quantity)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()



        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - (quantity * threadCount))
    }

    @Test
    fun sameTime100RequestWithOptimisticLock() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val quantity = 1L

        for (i in 1..threadCount)  {
            executorService.submit {
                try {
                    pacade.decrease(stockId, quantity)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()



        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - (quantity * threadCount))
    }

    @Test
    fun sameTime100RequestWithNamedLock() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val quantity = 1L

        for (i in 1..threadCount)  {
            executorService.submit {
                try {
                    namedLockFacade.decrease(stockId, quantity)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()



        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - (quantity * threadCount))
    }

    @Test
    fun sameTime100RequestWithLettuceLock() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val quantity = 1L

        for (i in 1..threadCount)  {
            executorService.submit {
                try {
                    lettuceLockStockFacade.decrease(stockId, quantity)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()



        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - (quantity * threadCount))
    }

    @Test
    fun sameTime100RequestWithRedissonLock() {
        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(32)
        val latch = CountDownLatch(threadCount)
        val quantity = 1L

        for (i in 1..threadCount)  {
            executorService.submit {
                try {
                    redissonLockStockFacade.decrease(stockId, quantity)
                } finally {
                    latch.countDown()
                }
            }
        }

        latch.await()



        val stock = stockRepository.findById(stockId).orElseThrow()

        Assertions.assertThat(stock.quantity).isEqualTo(baseQuantity - (quantity * threadCount))
    }

}
