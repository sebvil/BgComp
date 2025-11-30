package com.sebastianvm.bgcomp.mvvm

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

fun CoroutineScope.asCloseable() = CloseableCoroutineScope(coroutineScope = this)

/**
 * [CoroutineScope] that provides a method to [close] it, causing the rejection of any new tasks and
 * cleanup of all underlying resources associated with the scope.
 */
class CloseableCoroutineScope(override val coroutineContext: CoroutineContext) :
    AutoCloseable, CoroutineScope {

    constructor(coroutineScope: CoroutineScope) : this(coroutineScope.coroutineContext)

    override fun close() = coroutineContext.cancel()
}
