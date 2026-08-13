package com.shadow.ultrastudio.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val UltraDarkColors = darkColorScheme(
    background = DarkBackground,
    surface = DarkPanel,
    onBackground = DarkText,
    onSurface = DarkText,
    primary = DarkAccent,
    secondary = DarkAccent2,
    onPrimary = DarkText,
    outline = DarkBorder,
    error = DarkDanger,
)

private val UltraLightColors = lightColorScheme(
    background = LightBackground,
    surface = LightPanel,
    onBackground = LightText,
    onSurface = LightText,
    primary = LightAccent,
    secondary = LightAccent2,
    onPrimary = LightPanel,
    outline = LightBorder,
    error = LightDanger,
)

/** Mirrors the web app's muted/secondary text color, which Material3's default
 * color roles don't have a direct slot for. */
@Composable
fun mutedTextColor() = if (isSystemInDarkTheme()) DarkMuted else LightMuted

@Composable
fun UltraStudioTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) UltraDarkColors else UltraLightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = UltraTypography,
        content = content,
    )
}
