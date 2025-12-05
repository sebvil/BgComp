package com.sebastianvm.bgcomp.di

import com.sebastianvm.bgcomp.featureinterfaces.EnterPointsArguments
import com.sebastianvm.bgcomp.featureinterfaces.KombioGameSummaryArguments
import com.sebastianvm.bgcomp.features.kombio.enterpoints.ui.EnterPointsUi
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.EnterPointsState
import com.sebastianvm.bgcomp.features.kombio.enterpoints.viewmodel.EnterPointsUserAction
import com.sebastianvm.bgcomp.features.kombio.host.KombioGameHostProps
import com.sebastianvm.bgcomp.features.kombio.summary.ui.KombioGameSummaryUi
import com.sebastianvm.bgcomp.features.kombio.summary.viewmodel.KombioGameSummaryState
import com.sebastianvm.bgcomp.features.kombio.summary.viewmodel.KombioGameSummaryUserAction
import com.sebastianvm.bgcomp.mvvm.MvvmComponent
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import com.sebastianvm.bgcomp.mvvm.MvvmComponentInitializer
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.Provider
import kotlinx.coroutines.flow.StateFlow

@ContributesBinding(AppScope::class)
@Inject
class KombioGameHostMvvmComponentInitializer(
    private val kombioGameSummaryMvvmComponent:
        Provider<
            MvvmComponent.Factory<
                KombioGameSummaryArguments,
                KombioGameHostProps,
                KombioGameSummaryState,
                KombioGameSummaryUserAction,
                KombioGameSummaryUi,
            >
        >,
    private val enterPointsMvvmComponent:
        Provider<
            MvvmComponent.Factory<
                EnterPointsArguments,
                KombioGameHostProps,
                EnterPointsState,
                EnterPointsUserAction,
                EnterPointsUi,
            >
        >,
) : MvvmComponentInitializer<KombioGameHostProps> {

    override fun initialize(
        args: MvvmComponentArguments,
        props: StateFlow<KombioGameHostProps>,
    ): MvvmComponent<*, KombioGameHostProps, *, *, *> =
        when (args) {
            is KombioGameSummaryArguments -> kombioGameSummaryMvvmComponent().create(args, props)
            is EnterPointsArguments -> enterPointsMvvmComponent().create(args, props)
            else -> error("""$args not registered""")
        }
}
