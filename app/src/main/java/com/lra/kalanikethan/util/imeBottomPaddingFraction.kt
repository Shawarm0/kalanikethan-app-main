package com.lra.kalanikethan.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity


/**
 * Applies bottom padding to the [Modifier] based on the height of the IME (on-screen keyboard),
 * scaled by a specified [fraction].
 *
 * This is useful for adjusting UI elements when the keyboard appears, without consuming
 * the full IME inset, which may be necessary for partial movement of views or animations.
 *
 * @param fraction A value between 0f and 1f that determines what fraction of the IME bottom inset
 * should be applied as padding. Default is `1f`, meaning full IME height is used.
 *
 * @return A [Modifier] with the calculated bottom padding based on the IME inset.
 *
 * @sample
 * ```kotlin
 * Modifier
 *     .fillMaxSize()
 *     .imeBottomPaddingFraction(0.5f)
 * ```
 *
 * This would apply half of the IME height as bottom padding.
 */
@Composable
fun Modifier.imeBottomPaddingFraction(fraction: Float = 1f): Modifier = this.then(
    Modifier.padding(
        bottom = with(LocalDensity.current) {
            WindowInsets.ime.getBottom(this).toDp() * fraction
        }
    )
)