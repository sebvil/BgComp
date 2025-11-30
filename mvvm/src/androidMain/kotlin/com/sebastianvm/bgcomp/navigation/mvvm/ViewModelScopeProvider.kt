package com.sebastianvm.bgcomp.navigation.mvvm

import androidx.compose.ui.platform.AndroidUiDispatcher
import com.sebastianvm.bgcomp.mvvm.CloseableCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import kotlin.coroutines.EmptyCoroutineContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
interface ViewModelScopeProvider {

    @Provides
    fun provideViewModelScope(): CloseableCoroutineScope {
        val dispatcher =
            try {
                // In platforms where `Dispatchers.Main` is not available, Kotlin Multiplatform will
                // throw
                // an exception (the specific exception type may depend on the platform). Since
                // there's
                // no
                // direct functional alternative, we use `EmptyCoroutineContext` to ensure that a
                // coroutine
                // launched within this scope will run in the same context as the caller.
                Dispatchers.Main.immediate
            } catch (_: NotImplementedError) {
                // In Native environments where `Dispatchers.Main` might not exist (e.g., Linux):
                EmptyCoroutineContext
            } catch (_: IllegalStateException) {
                // In JVM Desktop environments where `Dispatchers.Main` might not exist (e.g.,
                // Swing):
                EmptyCoroutineContext
            }
        return CloseableCoroutineScope(
            coroutineContext = dispatcher + SupervisorJob() + AndroidUiDispatcher.Main
        )
    }
}
