package com.lra.kalanikethan.util

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity


/**
 * Applies bottom padding to the [Modifier] based on the height of the IME (on-screen keyboard),
 * scaled by a specified [fraction].
 *
 * This is useful for adjusting UI elements when the keyboard appears, without consuming
 * the full IME inset, which may be necessary for partial movement of views or animations.
 *
 * @param fraction A value between 0f and 1f that determines what fraction of the IME's bottom inset
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
 * This would apply half of the IME's height as bottom padding.
 */
fun Modifier.imeBottomPaddingFraction(fraction: Float = 1f): Modifier = composed {
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val reducedBottomPadding = with(density) { imeBottom.toDp() * fraction }
    this.then(Modifier.padding(bottom = reducedBottomPadding))
}