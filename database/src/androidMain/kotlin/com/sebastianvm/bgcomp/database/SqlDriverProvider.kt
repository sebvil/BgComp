package com.sebastianvm.bgcomp.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides

@ContributesTo(AppScope::class)
interface SqlDriverProvider {

    @Provides
    fun provideSqlDriver(appContext: Context): SqlDriver =
        AndroidSqliteDriver(schema = BgCompDatabase.Schema, context = appContext, name = "bgcomp.db")
            .also { driver ->
                // Enable foreign key constraints to ensure CASCADE deletes work
                driver.execute(null, "PRAGMA foreign_keys = ON", 0)
            }
}
