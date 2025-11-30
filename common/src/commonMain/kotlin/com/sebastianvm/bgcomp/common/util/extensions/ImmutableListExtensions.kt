package com.sebastianvm.bgcomp.common.util.extensions

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.PersistentMap
import kotlinx.collections.immutable.mutate
import kotlinx.collections.immutable.toPersistentMap

fun <K, V> ImmutableMap<K, V>.mutate(mutator: (MutableMap<K, V>) -> Unit): PersistentMap<K, V> {
    return this.toPersistentMap().mutate(mutator)
}
