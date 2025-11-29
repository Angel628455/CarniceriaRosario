package ucne.edu.carniceriarosario.presentation.categoria

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
fun CreateCategoriaScreen(
    viewModel: CategoriaCarnesViewModel = hiltViewModel(),
    categoriaId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = categoriaId != null && categoriaId > 0

    LaunchedEffect(uiState.successMessage) {
        if (uiState.successMessage != null && !uiState.isCreating && !uiState.isUpdating) {
            kotlinx.coroutines.delay(1500)
            onNavigateBack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Categoría" else "Nueva Categoría") },
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
            uiState.errorMessage?.let { ErrorMessage(it) }
            uiState.successMessage?.let { SuccessMessage(it) }

            OutlinedTextField(
                value = uiState.nombre,
                onValueChange = { viewModel.setNombre(it) },
                label = { Text("Nombre *") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage?.contains("nombre", ignoreCase = true) == true
            )

            OutlinedTextField(
                value = uiState.descripcion,
                onValueChange = { viewModel.setDescripcion(it) },
                label = { Text("Descripción") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(8.dp))

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
                        if (isEditMode) viewModel.updateCategoria()
                        else viewModel.createCategoria()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating && !uiState.isUpdating
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
fun CreateCategoriaScreenPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = "Res",
                    onValueChange = {},
                    label = { Text("Nombre *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Face, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = "Carnes de res premium",
                    onValueChange = {},
                    label = { Text("Descripción") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        }
    }
}