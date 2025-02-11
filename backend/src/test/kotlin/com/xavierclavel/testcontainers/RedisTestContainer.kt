package main.com.xavierclavel.containers

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.GenericContainer

object RedisTestContainer {

    val redis = GenericContainer("redis:alpine").apply {
        withExposedPorts(6379)
        start()
    }

    fun getRedisUri(): String {
        return "redis://${redis.host}:${redis.firstMappedPort}"
    }
}
