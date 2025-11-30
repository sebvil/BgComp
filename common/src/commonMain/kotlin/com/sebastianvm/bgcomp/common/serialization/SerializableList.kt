package com.sebastianvm.bgcomp.common.serialization

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Serializable

/**
 * Type alias for [ImmutableList] with automatic serialization support.
 *
 * Use this in `@Serializable` classes to get automatic ImmutableList serialization:
 * ```
 * @Serializable
 * data class MyData(
 *     val items: SerializableList<String>
 * )
 * ```
 *
 * This automatically uses [ImmutableListSerializer] for serialization/deserialization.
 */
typealias SerializableList<T> =
    @Serializable(with = ImmutableListSerializer::class) ImmutableList<T>
