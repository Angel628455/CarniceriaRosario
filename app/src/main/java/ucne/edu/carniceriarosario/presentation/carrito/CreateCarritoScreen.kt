package ucne.edu.carniceriarosario.presentation.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCarritoScreen(
    viewModel: CarritoDeComprasViewModel = hiltViewModel(),
    carritoId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = carritoId != null && carritoId > 0

    // Cargar carrito para edición
    LaunchedEffect(carritoId) {
        if (isEditMode) {
            val carrito = uiState.carritos.find { it.carritoId == carritoId }
            carrito?.let { viewModel.setCarritoForEdit(it) }
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
                title = { Text(if (isEditMode) "Editar Carrito" else "Nuevo Carrito") },
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
            uiState.errorMessage?.let { ErrorMessageCarrito(it) }
            uiState.successMessage?.let { SuccessMessageCarrito(it) }

            // Campo Cliente ID
            OutlinedTextField(
                value = uiState.clienteId,
                onValueChange = { viewModel.setClienteId(it) },
                label = { Text("ID del Cliente *") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.errorMessage?.contains("cliente", ignoreCase = true) == true
            )

            // Switch para compra
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Compra realizada?",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = uiState.compra,
                    onCheckedChange = { viewModel.setCompra(it) }
                )
            }

            // Información de productos
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Productos en el carrito",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.productos.size} productos",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (uiState.productos.isEmpty()) {
                        Text(
                            text = "No hay productos en el carrito",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.productos.forEach { producto ->
                                ProductoCarritoItem(producto = producto)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Monto Total: $${uiState.montoTotal}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

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
                        if (isEditMode) viewModel.updateCarrito()
                        else viewModel.createCarrito()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating && !uiState.isUpdating && uiState.clienteId.isNotBlank()
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

@Composable
fun ProductoCarritoItem(producto: ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Producto #${producto.productoId}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cantidad: ${producto.cantidad}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCarritoScreenPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = "CLI-001",
                    onValueChange = {},
                    label = { Text("ID del Cliente *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Productos en el carrito",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Monto Total: $150.75",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}