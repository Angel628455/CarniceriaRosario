package ucne.edu.carniceriarosario.presentation.cliente

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClienteScreen(
    viewModel: ClienteViewModel = hiltViewModel(),
    clienteId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = clienteId != null && clienteId > 0

    // Cargar cliente para edición
    LaunchedEffect(clienteId) {
        if (isEditMode) {
            val cliente = uiState.clientes.find { it.clienteId == clienteId }
            cliente?.let { viewModel.setClienteForEdit(it) }
        }
    }

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && !uiState.isCreating && !uiState.isUpdating) {
            kotlinx.coroutines.delay(1500)
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Cliente" else "Nuevo Cliente") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mensajes
            uiState.errorMessage?.let { ErrorMessageCliente(it) }
            uiState.successMessage?.let { SuccessMessageCliente(it) }

            // Campo Nombre
            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.setNombre(it) },
                label = { Text("Nombre *") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage?.contains("nombre", ignoreCase = true) == true
            )

            // Campo Apellidos
            OutlinedTextField(
                value = uiState.apellidos,
                onValueChange = { viewModel.setApellidos(it) },
                label = { Text("Apellidos") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Teléfono
            OutlinedTextField(
                value = uiState.telefono,
                onValueChange = { viewModel.setTelefono(it) },
                label = { Text("Teléfono") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botones de acción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating && !uiState.isUpdating
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (isEditMode) viewModel.updateCliente()
                        else viewModel.createCliente()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating && !uiState.isUpdating && uiState.nombre.isNotBlank()
                ) {
                    if (uiState.isCreating || uiState.isUpdating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isEditMode) "Actualizar" else "Guardar")
                }
            }

            Text(
                text = "* Campos requeridos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateClienteScreenPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = "Juan",
                    onValueChange = {},
                    label = { Text("Nombre *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = "Pérez",
                    onValueChange = {},
                    label = { Text("Apellidos") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = "123456789",
                    onValueChange = {},
                    label = { Text("Teléfono") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Phone, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        }
    }
}