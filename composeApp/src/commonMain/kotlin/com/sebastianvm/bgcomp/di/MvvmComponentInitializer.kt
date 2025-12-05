package com.sebastianvm.bgcomp.di

import com.sebastianvm.bgcomp.featureinterfaces.HomeArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameHostArguments
import com.sebastianvm.bgcomp.featureinterfaces.NewKombioGameArguments
import com.sebastianvm.bgcomp.features.home.ui.HomeUi
import com.sebastianvm.bgcomp.features.home.viewmodel.HomeState
import com.sebastianvm.bgcomp.features.home.viewmodel.HomeUserAction
import com.sebastianvm.bgcomp.features.kombio.newgame.ui.NewKombioGameUi
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.NewKombioGameState
import com.sebastianvm.bgcomp.features.kombio.newgame.viewmodel.NewKombioGameUserAction
import com.sebastianvm.bgcomp.mvvm.MvvmComponent
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import com.sebastianvm.bgcomp.navigation.ui.NavHostUi
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostState
import com.sebastianvm.bgcomp.navigation.viewmodel.NavHostUserAction
import com.sebastianvm.bgcomp.navigation.viewmodel.NavigationProps
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provider
import kotlinx.coroutines.flow.StateFlow

@ContributesBinding(AppScope::class)
@Inject
class MvvmComponentInitializer(
    private val homeMvvmComponent:
        Provider<
            MvvmComponent.Factory<HomeArguments, NavigationProps, HomeState, HomeUserAction, HomeUi>
        >,
    private val newKombioGameMvvmComponent:
        Provider<
            MvvmComponent.Factory<
                NewKombioGameArguments,
                NavigationProps,
                NewKombioGameState,
                NewKombioGameUserAction,
                NewKombioGameUi,
            >
        >,
    private val kombioGameHostMvvmComponentInitializer:
        Provider<
            MvvmComponent.Factory<
                KombioGameHostArguments,
                NavigationProps,
                NavHostState,
                NavHostUserAction,
                NavHostUi,
            >
        >,
) : MvvmComponentInitializer<NavigationProps> {

    override fun initialize(
        args: MvvmComponentArguments,
        props: StateFlow<NavigationProps>,
    ): MvvmComponent<*, NavigationProps, *, *, *> =
        when (args) {
            is HomeArguments -> homeMvvmComponent().create(args, props)
            is NewKombioGameArguments -> newKombioGameMvvmComponent().create(args, props)
            is KombioGameHostArguments ->
                kombioGameHostMvvmComponentInitializer().create(args, props)
            else -> error("""$args not registered""")
        }
}
