package com.sebastianvm.bgcomp.database.di

import app.cash.sqldelight.db.SqlDriver
import com.sebastianvm.bgcomp.database.BgCompDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface DatabaseProvider {

    @SingleIn(AppScope::class)
    @Provides
    private fun provideDatabase(driver: SqlDriver): BgCompDatabase {
        return BgCompDatabase.Companion(driver = driver)
    }
}
