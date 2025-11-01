import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration

/**
 * Generate the DDL for the next DB migration.
 */
fun main() {
    //System.setProperty("ddl.migration.pendingDropsFor", "1.23")
    val dbMigration = DbMigration.create()
    dbMigration.setPlatform(Platform.POSTGRES)
    dbMigration.setPathToResources("mail-service/src/main/resources")
    dbMigration.generateMigration()
}
