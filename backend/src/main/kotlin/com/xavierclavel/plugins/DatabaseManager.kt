package com.xavierclavel.plugins

import com.xavierclavel.main
import com.xavierclavel.utils.logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

object DatabaseManager {
    var mainDB : Database? = null

    private fun hikari(): HikariDataSource {
        val hikariConfig = HikariConfig("/db.properties")
        return HikariDataSource(hikariConfig)
    }

    fun init() {
        logger.info("Initializing database...")
        while (mainDB == null) {
            try {
                mainDB = DatabaseFactory.create(DatabaseConfig().apply {
                    it.dataSource =  hikari()
                })
            }catch (e:Exception) {
                logger.error { e.message }
                logger.error { "Failed to connect to database, retrying in 5 seconds" }
                runBlocking { (delay(5000)) }
            }
        }
        logger.info { "Successfully connected to database" }
    }
}
