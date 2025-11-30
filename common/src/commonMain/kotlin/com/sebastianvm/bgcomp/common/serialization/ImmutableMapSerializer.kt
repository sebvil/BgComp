package com.sebastianvm.bgcomp.common.serialization

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for [ImmutableMap] that delegates to a standard map serializer.
 *
 * @param keySerializer The serializer for the map keys
 * @param valueSerializer The serializer for the map values
 */
class ImmutableMapSerializer<K, V>(
    private val keySerializer: KSerializer<K>,
    private val valueSerializer: KSerializer<V>,
) : KSerializer<ImmutableMap<K, V>> {

    private val mapSerializer = MapSerializer(keySerializer, valueSerializer)

    override val descriptor: SerialDescriptor = mapSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableMap<K, V>) {
        mapSerializer.serialize(encoder, value.toMap())
    }

    override fun deserialize(decoder: Decoder): ImmutableMap<K, V> {
        return mapSerializer.deserialize(decoder).toImmutableMap()
    }
}
