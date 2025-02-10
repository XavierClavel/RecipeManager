package com.xavierclavel.plugins

import io.lettuce.core.ExperimentalLettuceCoroutinesApi
import io.lettuce.core.RedisClient
import io.lettuce.core.api.coroutines
import io.lettuce.core.api.coroutines.RedisCoroutinesCommands
import org.koin.core.component.KoinComponent

class RedisService(redisUrl: String): KoinComponent {
    private val client = RedisClient.create(redisUrl)
    private val connection = client.connect()

    @OptIn(ExperimentalLettuceCoroutinesApi::class)
    val redis: RedisCoroutinesCommands<String, String> = connection.coroutines()
}
