package ucne.edu.carniceriarosario.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    usuarioId: Int,
    viewModel: UsuarioViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar datos del usuario cuando se monta la pantalla
    LaunchedEffect(usuarioId) {
        viewModel.getUsuarioById(usuarioId)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil de Usuario") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoadingUsuarios) {
                CircularProgressIndicator(modifier = Modifier.padding(32.dp))
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario",
                            modifier = Modifier.size(80.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        ProfileItem("ID de Usuario", uiState.usuarioId.toString())
                        ProfileItem("Nombre de Usuario", uiState.userName)

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Información del Perfil",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}