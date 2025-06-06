package com.xavierclavel.plugins

import com.xavierclavel.models.jointables.query.QCookbookRecipe
import com.xavierclavel.models.jointables.query.QCookbookUser
import com.xavierclavel.models.jointables.query.QCustomIngredient
import com.xavierclavel.models.jointables.query.QFollow
import com.xavierclavel.models.jointables.query.QLike
import com.xavierclavel.models.jointables.query.QRecipeIngredient
import com.xavierclavel.models.query.QCookbook
import com.xavierclavel.models.query.QDietaryRestrictions
import com.xavierclavel.models.query.QIngredient
import com.xavierclavel.models.query.QRecipe
import com.xavierclavel.models.query.QUser
import com.xavierclavel.utils.logger
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.migration.MigrationConfig
import io.ebean.migration.MigrationRunner
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path
import kotlin.io.path.exists

object DatabaseManager {
    var mainDB : Database? = null

    fun getTables() = listOf(
        QRecipe(),
        QRecipeIngredient(),
        QCustomIngredient(),
        QUser(),
        QFollow(),
        QIngredient(),
        QLike(),
        QCookbook(),
        QCookbookUser(),
        QCookbookRecipe(),
        QDietaryRestrictions(),
    )

    private fun hikari(): HikariDataSource {
        val dbConfigPath = if (Path("/app/config/db.properties").exists()) {
            "/app/config/db.properties" // Path in VPS container
        } else {
            "/db.properties" // Local path
        }
        val hikariConfig = HikariConfig(dbConfigPath)
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
                logger.error { "Failed to connect to database at ${hikari().jdbcUrl}, retrying in 5 seconds" }
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
