package com.sebastianvm.bgcomp

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.sebastianvm.bgcomp.designsys.components.Scaffold
import com.sebastianvm.bgcomp.designsys.theme.BgCompTheme
import com.sebastianvm.bgcomp.featureinterfaces.HomeArguments
import com.sebastianvm.bgcomp.mvvm.LocalSerializersModule
import com.sebastianvm.bgcomp.mvvm.MvvmComponent
import com.sebastianvm.bgcomp.navigation.NavDestination
import com.sebastianvm.bgcomp.navigation.ui.NavHostUi
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostArguments
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostState
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostUserAction
import com.sebastianvm.bgcomp.ui.DateTimeFormatter
import com.sebastianvm.bgcomp.ui.LocalDateTimeFormatter
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provider
import kotlinx.serialization.modules.SerializersModule

@Inject
class App(
    private val navHostMvvmComponent:
        Provider<
            MvvmComponent.NoPropsFactory<
                NavHostArguments,
                NavHostState,
                NavHostUserAction,
                NavHostUi,
            >
        >,
    private val dateTimeFormatter: DateTimeFormatter,
    private val argsSerializerModule: SerializersModule,
) {
    @Composable
    operator fun invoke(modifier: Modifier = Modifier) {
        CompositionLocalProvider(
            LocalDateTimeFormatter provides dateTimeFormatter,
            LocalSerializersModule provides argsSerializerModule,
        ) {
            BgCompTheme {
                Scaffold(
                    modifier = modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0),
                ) { paddingValues ->
                    navHostMvvmComponent()
                        .create(NavHostArguments(NavDestination(HomeArguments)))
                        .Content(modifier = Modifier.padding(paddingValues))
                }
            }
        }
    }
}
