package com.xavierclavel.plugins

import com.xavierclavel.main
import com.xavierclavel.utils.logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.annotation.Platform
import io.ebean.config.DatabaseConfig
import io.ebean.dbmigration.DbMigration
import io.ebean.migration.MigrationConfig
import io.ebean.migration.MigrationRunner
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

        // Trigger migration runner manually
        val config = MigrationConfig()
        val migrationRunner = MigrationRunner(config)
        migrationRunner.run(hikari())
        logger.info("Database migrations applied")
    }
}
