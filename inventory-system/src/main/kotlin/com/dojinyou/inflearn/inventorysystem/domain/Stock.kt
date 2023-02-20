package com.dojinyou.inflearn.inventorysystem.domain

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "stocks")
class Stock(
    productId: UUID,
    quantity: Long,
): PrimaryKeyEntity() {
    @Column(nullable = false)
    var productId: UUID = productId
        private set

    @Column(nullable = false)
    var quantity: Long = quantity
        private set

    @Version
    var version:Long = 0

    fun decrease(quantity: Long) {
        require(quantity >= 0)
        require(this.quantity - quantity >= 0)

        this.quantity -= quantity
    }
}
