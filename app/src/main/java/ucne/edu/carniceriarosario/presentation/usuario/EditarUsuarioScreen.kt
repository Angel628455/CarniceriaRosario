package ucne.edu.carniceriarosario.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarUsuarioScreen(
    usuarioId: Int,
    viewModel: UsuarioViewModel = hiltViewModel(),
    onUsuarioActualizado: () -> Unit,
    onCancelar: () -> Unit,
    onEliminarUsuario: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Cargar el usuario cuando se monta la pantalla
    LaunchedEffect(usuarioId) {
        viewModel.getUsuarioById(usuarioId)
    }

    // Observar el estado de éxito para navegar
    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null) {
            if (uiState.successMessage!!.contains("actualizado")) {
                onUsuarioActualizado()
            } else if (uiState.successMessage!!.contains("eliminado")) {
                onEliminarUsuario()
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Editar Usuario") },
                navigationIcon = {
                    IconButton(onClick = onCancelar) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Cancelar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (uiState.isLoadingUsuarios) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.userName,
                        onValueChange = { viewModel.setUserName(it) },
                        label = { Text("Nombre de usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isUpdating
                    )

                    OutlinedTextField(
                        value = uiState.password,
                        onValueChange = { viewModel.setPassword(it) },
                        label = { Text("Contraseña") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        enabled = !uiState.isUpdating,
                        visualTransformation = if (uiState.passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingIcon = {
                            IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                Icon(
                                    imageVector = if (uiState.passwordVisible) {
                                        Icons.Default.Lock
                                    } else {
                                        Icons.Default.Lock
                                    },
                                    contentDescription = if (uiState.passwordVisible) {
                                        "Ocultar contraseña"
                                    } else {
                                        "Mostrar contraseña"
                                    }
                                )
                            }
                        }
                    )

                    uiState.errorMessage?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.updateUsuario() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !uiState.isUpdating
                    ) {
                        if (uiState.isUpdating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Actualizar Usuario")
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Confirmar eliminación") },
            text = {
                Text("¿Estás seguro de que deseas eliminar este usuario? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        viewModel.deleteUsuario(usuarioId)
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}