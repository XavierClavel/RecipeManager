package main

import io.ebean.annotation.Platform
import io.ebean.dbmigration.DbMigration

/**
 * Generate the DDL for the next DB migration.
 */
fun main() {
    // requires jvmTarget 1.8
    val dbMigration = DbMigration.create()
    dbMigration.setPlatform(Platform.POSTGRES)
    dbMigration.setPathToResources("backend/src/main/resources")
    dbMigration.generateMigration()
}
