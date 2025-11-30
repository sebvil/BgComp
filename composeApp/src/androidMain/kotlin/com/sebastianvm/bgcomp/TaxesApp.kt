package com.sebastianvm.bgcomp

import android.app.Application
import com.sebastianvm.bgcomp.di.AndroidAppGraph
import dev.zacsweers.metro.createGraphFactory

class bgcompApp : Application() {
    val appGraph by lazy { createGraphFactory<AndroidAppGraph.Factory>().create(this) }
}
