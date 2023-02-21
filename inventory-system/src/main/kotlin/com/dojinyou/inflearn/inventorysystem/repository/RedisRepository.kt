package com.dojinyou.inflearn.inventorysystem.repository

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisRepository(
    private val redisTemplate: RedisTemplate<String, String>
) {

    fun lock(key: String): Boolean {
        return redisTemplate.opsForValue()
            .setIfAbsent(key, "lock", Duration.ofMillis(3_000))!!
    }

    fun unLock(key: String): Boolean {
        return redisTemplate.delete(key)
    }

}
