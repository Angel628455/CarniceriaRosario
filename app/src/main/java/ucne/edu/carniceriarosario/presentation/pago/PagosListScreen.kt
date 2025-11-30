package ucne.edu.carniceriarosario.presentation.pago

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.delay

// Tus DTOs
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto
import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PagosListScreen(
    viewModel: PagosViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<PagosDto?>(null) }

    LaunchedEffect(uiState.successMessage, uiState.errorMessage) {
        uiState.successMessage?.let {
            delay(3000)
            viewModel.clearMessages()
        }
        uiState.errorMessage?.let {
            delay(3000)
            viewModel.clearMessages()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pagos y Detalles") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar pago",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Barra de búsqueda
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Buscar por cliente, ID o monto...") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                },
                trailingIcon = {
                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Mensajes
            uiState.successMessage?.let { SuccessMessagePago(it) }
            uiState.errorMessage?.let { ErrorMessagePago(it) }

            // Lista de pagos
            when {
                uiState.isLoadingPagos -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorPagos != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(48.dp)
                            )
                            Text(
                                text = uiState.errorPagos ?: "Error desconocido",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.loadPagos() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                uiState.pagosFiltrados.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Send,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = if (uiState.searchQuery.isBlank())
                                    "No hay pagos registrados"
                                else
                                    "No se encontraron pagos",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.pagosFiltrados,
                            key = { it.pagoId }
                        ) { pago ->
                            PagoConDetallesItem(
                                pago = pago,
                                detalles = uiState.detallesPagos.filter { it.pagoId == pago.pagoId },
                                onEdit = { onNavigateToEdit(pago.pagoId) },
                                onDelete = { showDeleteDialog = pago }
                            )
                        }
                    }
                }
            }
        }
    }

    showDeleteDialog?.let { pago ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Eliminar pago") },
            text = {
                Text("¿Está seguro que desea eliminar el pago #${pago.pagoId} del cliente '${pago.clienteId}'? Esta acción también eliminará todos sus detalles.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletePago(pago.pagoId)
                        showDeleteDialog = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun PagoConDetallesItem(
    pago: PagosDto,
    detalles: List<DetallesPagosDto>,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onEdit),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Encabezado del pago
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pago #${pago.pagoId}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cliente: ${pago.clienteId}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Información del pago
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Monto Total:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${pago.montoPagado}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Column {
                    Text(
                        text = "Fecha:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = pago.fechaPago,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column {
                    Text(
                        text = "Detalles:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${detalles.size} detalles",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            // Lista de detalles del pago
            if (detalles.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Detalles del Pago:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                detalles.forEach { detalle ->
                    DetallePagoItem(detalle = detalle)
                }
            }
        }
    }
}

@Composable
fun DetallePagoItem(detalle: DetallesPagosDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
                    text = "Pedido #${detalle.pedidoId}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Método Pago ID: ${detalle.metodoPagoId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "$${detalle.montoPagado}",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun SuccessMessagePago(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun ErrorMessagePago(message: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = message,
                color = MaterialTheme.colorScheme.onErrorContainer
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PagoConDetallesItemPreview() {
    MaterialTheme {
        PagoConDetallesItem(
            pago = PagosDto(
                pagoId = 1,
                clienteId = "CLI-001",
                montoPagado = 150.75f,
                fechaPago = "2024-01-15",
                detallesPagos = emptyList()
            ),
            detalles = listOf(
                DetallesPagosDto(
                    detallePagoId = 1,
                    pedidoId = 101,
                    pagoId = 1,
                    metodoPagoId = 1,
                    montoPagado = 75.50f
                ),
                DetallesPagosDto(
                    detallePagoId = 2,
                    pedidoId = 102,
                    pagoId = 1,
                    metodoPagoId = 2,
                    montoPagado = 75.25f
                )
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}