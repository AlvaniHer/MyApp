package com.vani.myapp.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Carta,        // Azul
    onPrimary = blanco, // Color del texto sobre el botón azul
    secondary = letracarta, // Rosa

    onSecondary = negro,
    background = fondo,     // Crema/Rosa claro
    onBackground = fondo, // Color del texto sobre el fondo
    tertiary = titulos,     // Azul títulos
    onTertiary = blanco,
    surface = fondo,        // Color de las superficies (como Cards)
    onSurface = negro,
    error=red
)

private val LightColorScheme = lightColorScheme(
    primary = Carta,
    onPrimary = blanco,
    secondary = letracarta,
    onSecondary = negro,
    background = fondo,
    onBackground = fondo,
    tertiary = titulos,
    onTertiary = blanco,
    surface = fondo,
    onSurface = negro,
    error=red
)

@Composable
fun MyAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}