package com.mystxc.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** iOS 18+ glass tokens: the translucent dark base, the white sheen, the top-lit border. */
object GlassTokens {
    /** Layered translucent dark glass base: #0E1526 at 45%-60% opacity. */
    val SurfaceTint = Color(0xFF0E1526)
    /** Subtle vertical white sheen over the base: #FFFFFF 12% at the top, fading to #FFFFFF 3% at the bottom. */
    val Sheen = Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.12f), Color.White.copy(alpha = 0.03f)))
    /** 1dp top-lit specular border: #FFFFFF 24% at the top, fading to #FFFFFF 6% at the bottom. */
    val BorderSheen = Brush.verticalGradient(listOf(Color.White.copy(alpha = 0.24f), Color.White.copy(alpha = 0.06f)))
    /** Corner radii: 20dp cards/panels, 28dp pill bars. */
    val CardRadius = 20.dp
    val PillRadius = 28.dp
}

/**
 * The glass modifier (iOS 18+ glassmorphism): a clipped RoundedCornerShape(radius), a layered translucent
 * dark glass base (#0E1526 at 45%-60%), a subtle vertical white sheen over it, and a 1dp top-lit specular border.
 */
fun Modifier.glassSurface(
    radius: Dp = GlassTokens.CardRadius,
    tintAlpha: Float = 0.55f
): Modifier = this
    .clip(RoundedCornerShape(radius))
    .background(GlassTokens.SurfaceTint.copy(alpha = tintAlpha))
    .drawWithContent {
        drawRect(GlassTokens.Sheen, size = this.size)
        drawContent()
    }
    .border(1.dp, GlassTokens.BorderSheen, RoundedCornerShape(radius))

/** A frosted-glass card: a glass surface in a column, used for chat panels, sheets and the About card. */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: Dp = GlassTokens.CardRadius,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(modifier = modifier.glassSurface(radius), content = content)
}

/** A frosted-glass pill bar: the translucent navigation bar with the glow. */
@Composable
fun GlassBottomBar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier.glassSurface(GlassTokens.PillRadius), contentAlignment = Alignment.Center) {
        content()
    }
}

/** A frosted-glass sheet: a dimmed scrim behind a glass card (the sibling element covers the dim). */
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
                interactionSource = remember { MutableInteractionSource() },
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
