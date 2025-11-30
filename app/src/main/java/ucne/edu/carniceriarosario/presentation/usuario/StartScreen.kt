package ucne.edu.carniceriarosario.presentation.usuario

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun StartScreen(
    onSplashComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000) // 2 segundos
        onSplashComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(android.R.drawable.ic_dialog_info), // Cambia por tu logo
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )
    }
}