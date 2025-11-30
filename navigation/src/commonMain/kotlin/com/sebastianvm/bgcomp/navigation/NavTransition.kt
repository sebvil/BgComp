package com.sebastianvm.bgcomp.navigation

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import kotlinx.serialization.Serializable

@Serializable
sealed class NavTransition {

    @Serializable data object None : NavTransition()

    @Serializable
    data class Fade(val durationMillis: Int = DEFAULT_DURATION_MILLIS) : NavTransition()

    @Serializable
    data class Slide(val durationMillis: Int = DEFAULT_DURATION_MILLIS) : NavTransition()

    @Serializable
    data class SlideFromEnd(val durationMillis: Int = DEFAULT_DURATION_MILLIS) : NavTransition()

    @Serializable
    data class Scale(val durationMillis: Int = DEFAULT_DURATION_MILLIS) : NavTransition()

    @Serializable
    data class FadeThrough(val durationMillis: Int = DEFAULT_DURATION_MILLIS) : NavTransition()

    companion object {
        val Default = Slide()

        private const val DEFAULT_DURATION_MILLIS = 300
    }
}

fun NavTransition.toContentTransform(isForward: Boolean): ContentTransform {
    return when (this) {
        is NavTransition.None -> {
            EnterTransition.None togetherWith ExitTransition.None
        }

        is NavTransition.Fade -> {
            fadeIn(animationSpec = tween(durationMillis)) togetherWith
                fadeOut(animationSpec = tween(durationMillis))
        }

        is NavTransition.Slide -> {
            val enter =
                if (isForward) {
                    // Forward: New screen slides in from right
                    slideInHorizontally(
                        initialOffsetX = { it },
                        animationSpec = tween(durationMillis),
                    ) + fadeIn(animationSpec = tween(durationMillis))
                } else {
                    // Backward: Previous screen slides in from left (with parallax)
                    slideInHorizontally(
                        initialOffsetX = { -it / 3 },
                        animationSpec = tween(durationMillis),
                    ) + fadeIn(animationSpec = tween(durationMillis))
                }

            val exit =
                if (isForward) {
                    // Forward: Current screen slides out to left (with parallax)
                    slideOutHorizontally(
                        targetOffsetX = { -it / 3 },
                        animationSpec = tween(durationMillis),
                    ) + fadeOut(animationSpec = tween(durationMillis))
                } else {
                    // Backward: Current screen slides out to right
                    slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(durationMillis),
                    ) + fadeOut(animationSpec = tween(durationMillis))
                }

            enter togetherWith exit
        }

        is NavTransition.SlideFromEnd -> {
            val enter =
                if (isForward) {
                    // Forward: New screen slides in from left
                    slideInHorizontally(
                        initialOffsetX = { -it },
                        animationSpec = tween(durationMillis),
                    ) + fadeIn(animationSpec = tween(durationMillis))
                } else {
                    // Backward: Previous screen slides in from right (with parallax)
                    slideInHorizontally(
                        initialOffsetX = { it / 3 },
                        animationSpec = tween(durationMillis),
                    ) + fadeIn(animationSpec = tween(durationMillis))
                }

            val exit =
                if (isForward) {
                    // Forward: Current screen slides out to right (with parallax)
                    slideOutHorizontally(
                        targetOffsetX = { it / 3 },
                        animationSpec = tween(durationMillis),
                    ) + fadeOut(animationSpec = tween(durationMillis))
                } else {
                    // Backward: Current screen slides out to left
                    slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = tween(durationMillis),
                    ) + fadeOut(animationSpec = tween(durationMillis))
                }

            enter togetherWith exit
        }

        is NavTransition.Scale -> {
            val enter =
                scaleIn(initialScale = 0.8f, animationSpec = tween(durationMillis)) +
                    fadeIn(animationSpec = tween(durationMillis))

            val exit =
                scaleOut(targetScale = 0.8f, animationSpec = tween(durationMillis)) +
                    fadeOut(animationSpec = tween(durationMillis))

            enter togetherWith exit
        }

        is NavTransition.FadeThrough -> {
            fadeIn(
                animationSpec = tween(durationMillis / 2, delayMillis = durationMillis / 2)
            ) togetherWith fadeOut(animationSpec = tween(durationMillis / 2))
        }
    }
}
