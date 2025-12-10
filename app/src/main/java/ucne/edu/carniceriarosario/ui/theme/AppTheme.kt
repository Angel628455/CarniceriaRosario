package ucne.edu.carniceriarosario.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    isDarkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val LightColors = lightColorScheme(
        primary = Color(0xFFB71C1C),
        onPrimary = Color.White,
        background = Color(0xFFFFF3F2),
        onBackground = Color(0xFF2B1A18),
        surface = Color.White,
        onSurface = Color(0xFF2B1A18)
    )

    val DarkColors = darkColorScheme(
        primary = Color(0xFFD32F2F),
        onPrimary = Color.Black,
        background = Color(0xFF121212),
        onBackground = Color(0xFFFFEAEA),
        surface = Color(0xFF1E1E1E),
        onSurface = Color(0xFFFFEAEA)
    )

    val colors = if (isDarkMode) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}