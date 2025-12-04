package com.sebastianvm.bgcomp.common.serialization

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer for [ImmutableList] that delegates to a standard list serializer.
 *
 * @param elementSerializer The serializer for the list elements
 */
class ImmutableListSerializer<T>(private val elementSerializer: KSerializer<T>) :
    KSerializer<ImmutableList<T>> {

    private val listSerializer = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: ImmutableList<T>) {
        encoder.encodeSerializableValue(listSerializer, value.toList())
    }

    override fun deserialize(decoder: Decoder): ImmutableList<T> {
        return decoder.decodeSerializableValue(listSerializer).toImmutableList()
    }
}
