package main

import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration

/**
 * Generate the DDL for the next DB migration.
 */
fun main() {
    //System.setProperty("ddl.migration.pendingDropsFor", "1.13")
    val dbMigration = DbMigration.create()
    dbMigration.setPlatform(Platform.POSTGRES)
    dbMigration.setPathToResources("backend/src/main/resources")
    dbMigration.generateMigration()
}
