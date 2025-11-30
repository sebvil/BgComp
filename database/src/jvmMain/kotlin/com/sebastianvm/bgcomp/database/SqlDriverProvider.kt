package com.sebastianvm.bgcomp.database

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlCursor
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface SqlDriverProvider {
    @Provides
    fun provideSqlDriver(): SqlDriver =
        JdbcSqliteDriver("jdbc:sqlite:bgcomp.db").also { driver ->
            if (driver.version == 0L) {
                BgCompDatabase.Schema.create(driver)
                driver.version = 1L
            }
            // Enable foreign key constraints to ensure CASCADE deletes work
            driver.execute(identifier = null, sql = "PRAGMA foreign_keys = ON", parameters = 0)
        }

    private var SqlDriver.version: Long
        get() {
            val queryResult =
                executeQuery(
                    identifier = null,
                    sql = "PRAGMA user_version;",
                    mapper = { sqlCursor: SqlCursor -> QueryResult.Value(sqlCursor.getLong(0)) },
                    parameters = 0,
                    binders = null,
                )
            return queryResult.value!!
        }
        set(value) {
            execute(
                identifier = null,
                sql = "PRAGMA user_version = $value;",
                parameters = 0,
                binders = null,
            )
        }
}
