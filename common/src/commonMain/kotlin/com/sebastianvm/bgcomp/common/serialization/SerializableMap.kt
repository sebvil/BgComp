package com.sebastianvm.bgcomp.common.serialization

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.serialization.Serializable

/**
 * Type alias for [ImmutableMap] with automatic serialization support.
 *
 * Use this in `@Serializable` classes to get automatic ImmutableMap serialization:
 * ```
 * @Serializable
 * data class MyData(
 *     val map: SerializableMap<String, Int>
 * )
 * ```
 *
 * This automatically uses [ImmutableMapSerializer] for serialization/deserialization.
 */
typealias SerializableMap<K, V> =
    @Serializable(with = ImmutableMapSerializer::class) ImmutableMap<K, V>
