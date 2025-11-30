package com.sebastianvm.bgcomp.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import kotlin.jvm.JvmInline
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

@Immutable
interface UiString {

    @get:Composable val value: String

    suspend fun getValue(): String

    /** Stable ID to be used when the UiString needs to be used as a key for a composable. */
    val id: Any

    companion object {
        operator fun invoke(text: String): UiString = RawString(text)

        operator fun invoke(resource: StringResource): UiString = StringRes(resource)

        fun String.toUiString(): UiString = RawString(this)

        fun StringResource.toUiString(): UiString = StringRes(this)
    }

    @JvmInline
    private value class RawString(val text: String) : UiString {
        override val value: String
            @Composable get() = text

        override suspend fun getValue(): String = text

        override val id: Any
            get() = text
    }

    @JvmInline
    private value class StringRes(val resource: StringResource) : UiString {
        override val value: String
            @Composable get() = stringResource(resource)

        override suspend fun getValue(): String = getString(resource)

        override val id: Any
            get() = resource.key
    }
}
