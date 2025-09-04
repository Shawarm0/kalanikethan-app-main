package com.lra.kalanikethan.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

/**
 * Adds a top border to a composable using a custom stroke width and color.
 *
 * This extension function uses [Modifier.drawBehind] to draw a horizontal line
 * along the top edge of the composable. The line is centered vertically within
 * the stroke's thickness so that half of it lies inside the composable's bounds
 * and half outside.
 *
 * Example usage:
 * ```
 * Box(
 *     modifier = Modifier
 *         .fillMaxWidth()
 *         .height(100.dp)
 *         .topBorder(2.dp, Color.Black)
 * )
 * ```
 *
 * @param strokeWidth The thickness of the border in [Dp].
 * @param color The [Color] of the border.
 * @return A [Modifier] with a top border applied.
 */
fun Modifier.topBorder(strokeWidth: Dp, color: Color) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = strokeWidth.toPx()
        val width = size.width
        val y = strokeWidthPx / 2

        drawLine(
            color = color,
            start = Offset(x = 0f, y = y),
            end = Offset(x = width, y = y),
            strokeWidth = strokeWidthPx
        )
    }
)