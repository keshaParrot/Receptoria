package github.projectgroup.receptoriaApp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Orange300,
    onPrimary = Brown900,
    secondary = Green300,
    onSecondary = Brown900,
    tertiary = Red300,
    background = Brown800,
    surface = Brown900,
    onBackground = Cream50,
    onSurface = Cream50,
    error = Red700
)

private val LightColorScheme = lightColorScheme(
    primary = Orange500,
    onPrimary = Cream50,
    secondary = Green500,
    onSecondary = Cream50,
    tertiary = Red700,
    background = Cream50,
    surface = Cream100,
    onBackground = Brown900,
    onSurface = Brown900,
    error = Red700
)

@Composable
fun ReceptoriaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
