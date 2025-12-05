package com.sebastianvm.bgcomp.navigation.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.navigation.ui.NavHostUi
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@com.sebastianvm.bgcomp.mvvm.codegen.MvvmComponent(
    argsClass = NavHostArguments::class,
    uiClass = NavHostUi::class,
)
@AssistedInject
class NavHostViewModel(
    @Assisted private val arguments: NavHostArguments,
    mvvmComponentInitializer: MvvmComponentInitializer<NavigationProps>,
) : BaseNavHostViewModel<Nothing, NavigationProps>(arguments, mvvmComponentInitializer) {

    @Composable
    override fun childrenProps(navigationProps: NavigationProps): StateFlow<NavigationProps> {
        return remember { MutableStateFlow(navigationProps) }
    }
}
