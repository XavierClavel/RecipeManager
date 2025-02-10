package main.com.xavierclavel.containers

import com.xavierclavel.config.DbConfig
import com.xavierclavel.plugins.DatabaseManager
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

object PostgresTestContainer {

    private val container = PostgreSQLContainer(DockerImageName.parse("postgres:13.16")).apply {
        withDatabaseName("test_db")
        withUsername("test_user")
        withPassword("test_pass")
        start()
    }

    val testDbConfig = DbConfig(
        jdbcUrl = container.jdbcUrl,
        user = container.username,
        password = container.password
    )

    fun startContainer() {
        container.start()
        DatabaseManager.configure(testDbConfig)
    }

    fun stopContainer() {
        container.stop()
    }
}
