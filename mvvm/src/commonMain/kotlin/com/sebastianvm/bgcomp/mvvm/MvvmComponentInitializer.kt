package com.sebastianvm.bgcomp.mvvm

import kotlinx.coroutines.flow.StateFlow

interface MvvmComponentInitializer<P : Props> {

    fun initialize(args: MvvmComponentArguments, props: StateFlow<P>): MvvmComponent<*, P, *, *, *>
}
