package com.sebastianvm.bgcomp.navigation

import androidx.compose.ui.unit.IntOffset
import androidx.navigation3.runtime.NavKey
import androidx.window.core.layout.WindowSizeClass
import com.sebastianvm.bgcomp.mvvm.MvvmComponentArguments
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class NavDestination(
    val args: MvvmComponentArguments,
    val presentationModes: PresentationModes = PresentationModes(),
) : NavKey

fun PresentationModes.presentationMode(windowSizeClass: WindowSizeClass): PresentationMode {
    if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)) {
        return forLargerScreens
    }
    return default
}

@Serializable
data class PresentationModes(
    val default: PresentationMode = PresentationMode.Screen(),
    val forLargerScreens: PresentationMode = default,
) {

    companion object {
        fun bottomSheetOrPopUpIfLarge(positionForPopUp: IntOffset) =
            PresentationModes(
                PresentationMode.BottomSheet,
                PresentationMode.PopUp(position = positionForPopUp),
            )
    }
}

@Serializable
sealed class PresentationMode {

    @Serializable
    data class Screen(val transition: NavTransition = NavTransition.Default) : PresentationMode()

    @Serializable data object BottomSheet : PresentationMode()

    @Serializable
    data class PopUp(@Serializable(with = IntOffsetSerializer::class) val position: IntOffset) :
        PresentationMode()
}

class IntOffsetSerializer : KSerializer<IntOffset> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("IntOffsetAsLongSerializer", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: IntOffset) {
        encoder.encodeLong(value.packedValue)
    }

    override fun deserialize(decoder: Decoder): IntOffset {
        return IntOffset(packedValue = decoder.decodeLong())
    }
}
