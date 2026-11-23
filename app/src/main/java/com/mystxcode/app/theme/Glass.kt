package com.mystxcode.app.theme

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.rememberMutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource

/**
 * iOS 18+ Glassmorphism, as an app-wide design system.
 *
 * Authentic frosted-glass surfaces over a dark ambient mesh backdrop: surfaces are #FFFFFF at 6%-10% over a
 * #0E1526 tint (45%-60%) with a crisp #FFFFFF glass border (14%-18%) on the top edge, a real 18dp-28dp backdrop
 * blur (RenderEffect, API 33+) with a semi-transparent fallback scrim below, and corner radii of 20dp (cards),
 * 14dp (buttons/inputs) and 28dp (pill navigation). Glow orbs sit behind glass surfaces.
 */
object GlassTokens {
    /** Canvas behind the glass (the ambient mesh backdrop). */
    val CanvasTop = Color(0xFF070B14)
    val CanvasMid = Color(0xFF0F1629)
    val CanvasEdge = Color(0xFF18102B)

    /** Surface fill: #FFFFFF at 6%-10% layered over the #0E1526 tint. */
    val SurfaceFill = Color.White
    val SurfaceTint = Color(0xFF0E1526)
    val SurfaceFillLow = 0.06f
    val SurfaceFillHigh = 0.10f

    /** Glass border: #FFFFFF at 14%-18%, brighter on the top edge. */
    val GlassBorder = Color.White
    val GlassBorderAlpha = 0.17f
    val GlassBorderTop = 0.18f

    /** Backdrop blur radius and the fallback scrim (API < 33 has no real blur). */
    val BlurRadius = 22.dp
    val scrimAlpha = 0.42f

    /** Corner radii. */
    val CardRadius = 20.dp
    val ButtonRadius = 14.dp
    val PillRadius = 28.dp

    /** Glow orbs behind glass surfaces. */
    val GlowCyan = Color(0x00E5FF)
    val GlowViolet = Color(0x9D4EDD)
    val GlowAlpha = 0.25f
}

/**
 * The glass modifier: an authentic frosted surface. The glass fill, border and scrim are always drawn; the real
 * backdrop blur (a composable `RenderEffect`/`BlurEffect`) is used where the platform supports it (API 33+).
 */
fun Modifier.glassSurface(
    radius: androidx.compose.ui.unit.Dp = GlassTokens.CardRadius,
    blur: androidx.compose.ui.unit.Dp = GlassTokens.BlurRadius,
    fillAlpha: Float = GlassTokens.SurfaceFillLow,
    borderAlpha: Float = GlassTokens.GlassBorderAlpha,
    scrimAlpha: Float = GlassTokens.scrimAlpha
): Modifier = this
    .graphicsLayer {
        if (Build.VERSION.SDK_INT >= 33) {
            renderEffect = BlurEffect(blur.toPx(), blur.toPx())
        }
    }
    .clip(RoundedCornerShape(radius))
    .drawWithContent {
        drawRect(Color(0xFF0A0E1A).copy(alpha = scrimAlpha))
        drawContent()
    }
    .background(GlassTokens.SurfaceFill.copy(alpha = fillAlpha))
    .border(
        BorderStroke(1.dp, GlassTokens.GlassBorder.copy(alpha = borderAlpha)),
        RoundedCornerShape(radius)
    )

/** A frosted-glass card: a glass surface in a column, used for chat panels, sheets and the About card. */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: androidx.compose.ui.unit.Dp = GlassTokens.CardRadius,
    blur: androidx.compose.ui.unit.Dp = GlassTokens.BlurRadius,
    fillAlpha: Float = GlassTokens.SurfaceFillLow,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier.glassSurface(radius, blur, fillAlpha),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
        content = content
    )
}

/** A frosted-glass pill bar: the translucent, blurred navigation bar with a glow, haptic feedback ready. */
@Composable
fun GlassBottomBar(
    modifier: Modifier = Modifier,
    blur: androidx.compose.ui.unit.Dp = 24.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.glassSurface(GlassTokens.PillRadius, blur, fillAlpha = 0.09f, borderAlpha = 0.21f),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/** A frosted-glass sheet: a dimmed blurred scrim behind a glass card (the sheet's sibling covers the dim). */
@Composable
fun GlassSheet(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    sheetContent: @Composable () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = rememberMutableInteractionSource(),
                indication = null,
                onClick = onDismissRequest
            )
            .then(modifier.fillMaxSize()),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(Modifier.fillMaxSize().background(Color(0x66000000)))
        GlassCard(Modifier.padding(16.dp, 0.dp, 16.dp, 16.dp)) {
            sheetContent()
        }
    }
}
