import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ebean.Database
import io.ebean.DatabaseFactory
import io.ebean.config.DatabaseConfig
import io.ebean.migration.MigrationConfig
import io.ebean.migration.MigrationRunner
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import models.query.QFollower
import models.query.QUser
import shared.utils.logger

object DatabaseManager {
    var mainDB : Database? = null

    fun getTables() = listOf(
        QUser(),
        QFollower(),
    )

    private fun hikari(): HikariDataSource {
        val config = HikariConfig()
        config.jdbcUrl = System.getenv("JDBC_URL")
        config.username = System.getenv("POSTGRES_USER")
        config.password = System.getenv("POSTGRES_PASSWORD")
        return HikariDataSource(config)
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
