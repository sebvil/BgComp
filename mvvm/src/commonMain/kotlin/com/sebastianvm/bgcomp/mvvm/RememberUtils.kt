package com.sebastianvm.bgcomp.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.savedstate.SavedState
import androidx.savedstate.compose.serialization.serializers.MutableStateSerializer
import androidx.savedstate.compose.serialization.serializers.SnapshotStateListSerializer
import androidx.savedstate.compose.serialization.serializers.SnapshotStateMapSerializer
import androidx.savedstate.serialization.SavedStateConfiguration
import androidx.savedstate.serialization.decodeFromSavedState
import androidx.savedstate.serialization.encodeToSavedState
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.plus

@Composable
inline fun <reified T> serializableSaver(): Saver<T, SavedState> {
    val localSerializersModule = LocalSerializersModule.current
    val config = SavedStateConfiguration {
        serializersModule +=
            SerializersModule {
                contextual(MutableState::class) { elementSerializers ->
                    MutableStateSerializer(elementSerializers.first())
                }
                contextual(SnapshotStateList::class) { elementSerializers ->
                    SnapshotStateListSerializer(elementSerializers.first())
                }
                contextual(SnapshotStateMap::class) { elementSerializers ->
                    SnapshotStateMapSerializer(elementSerializers.first(), elementSerializers[1])
                }
            } + localSerializersModule
    }
    return Saver(
        save = { encodeToSavedState(it, config) },
        restore = { decodeFromSavedState(it, config) },
    )
}

@Composable
inline fun <reified T : Any> rememberSerializable(vararg inputs: Any?, noinline init: () -> T): T {
    return rememberSaveable(*inputs, saver = serializableSaver<T>(), init = init)
}
