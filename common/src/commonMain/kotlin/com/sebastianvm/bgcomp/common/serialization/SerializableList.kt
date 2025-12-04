package com.sebastianvm.bgcomp.common.serialization

import kotlinx.collections.immutable.ImmutableList
import kotlinx.serialization.Contextual
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
 * This uses contextual serialization with [ImmutableListSerializer].
 * Make sure to register the serializer in your SerializersModule.
 */
typealias SerializableList<T> =
    @Serializable(with = ImmutableListSerializer::class) ImmutableList<T>
