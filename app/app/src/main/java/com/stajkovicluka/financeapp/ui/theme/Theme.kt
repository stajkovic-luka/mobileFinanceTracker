package com.stajkovicluka.financeapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Material 3 tema aplikacije
private val LightColorScheme = lightColorScheme(
    primary = Orange,
    onPrimary = Charcoal,
    secondary = Brown,
    onSecondary = Cream,
    tertiary = Yellow,
    onTertiary = Charcoal,
    background = Cream,
    onBackground = Charcoal,
    surface = Cream,
    onSurface = Charcoal
)

private val DarkColorScheme = darkColorScheme(
    primary = LightOrange,
    onPrimary = DarkBrown,
    secondary = LightBrown,
    onSecondary = DarkBrown,
    tertiary = Yellow,
    onTertiary = DarkBrown,
    background = DarkBrown,
    onBackground = Cream,
    surface = DarkBrown,
    onSurface = Cream
)

@Composable
fun FinanceAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
