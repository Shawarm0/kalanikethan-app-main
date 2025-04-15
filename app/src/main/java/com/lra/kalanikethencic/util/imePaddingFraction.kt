package com.lra.kalanikethencic.util


import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalDensity


fun Modifier.imePaddingFraction(fraction: Float = 1f): Modifier = composed {
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(LocalDensity.current)
    val reducedBottomPadding = with(density) { imeBottom.toDp() * fraction }
    this.then(Modifier.padding(bottom = reducedBottomPadding))
}
