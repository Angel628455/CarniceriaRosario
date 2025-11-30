package ucne.edu.carniceriarosario.presentation.pedido

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Warning
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
import ucne.edu.carniceriarosario.data.remote.dto.PedidosDto
import ucne.edu.carniceriarosario.data.remote.dto.EstadosDto

// Tu ViewModel
import ucne.edu.carniceriarosario.presentation.pedido.PedidosViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PedidosListScreen(
    viewModel: PedidosViewModel = hiltViewModel(),
    onNavigateToCreate: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteDialog by remember { mutableStateOf<PedidosDto?>(null) }

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
                title = { Text("Pedidos") },
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
                    contentDescription = "Agregar pedido",
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
                placeholder = { Text("Buscar por cliente, ID o estado...") },
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
            uiState.successMessage?.let { SuccessMessagePedido(it) }
            uiState.errorMessage?.let { ErrorMessagePedido(it) }

            // Lista de pedidos
            when {
                uiState.isLoadingPedidos -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                uiState.errorPedidos != null -> {
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
                                text = uiState.errorPedidos ?: "Error desconocido",
                                color = MaterialTheme.colorScheme.error
                            )
                            Button(onClick = { viewModel.loadPedidos() }) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
                uiState.pedidosFiltrados.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = if (uiState.searchQuery.isBlank())
                                    "No hay pedidos registrados"
                                else
                                    "No se encontraron pedidos",
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
                            items = uiState.pedidosFiltrados,
                            key = { it.pedidoId }
                        ) { pedido ->
                            PedidoItem(
                                pedido = pedido,
                                onEdit = { onNavigateToEdit(pedido.pedidoId) },
                                onDelete = { showDeleteDialog = pedido }
                            )
                        }
                    }
                }
            }
        }
    }

    showDeleteDialog?.let { pedido ->
        AlertDialog(
            onDismissRequest = { showDeleteDialog = null },
            icon = {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            },
            title = { Text("Eliminar pedido") },
            text = {
                Text("¿Está seguro que desea eliminar el pedido #${pedido.pedidoId} del cliente '${pedido.clienteId}'?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deletePedido(pedido.pedidoId)
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
fun PedidoItem(
    pedido: PedidosDto,
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
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Pedido #${pedido.pedidoId}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Cliente: ${pedido.clienteId ?: "N/A"}",
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

            // Información adicional del pedido
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
                        text = "$${pedido.montoTotal}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = "Productos:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = pedido.productosOrdenados.size.toString(),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column {
                    Text(
                        text = "Estado:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = pedido.estados?.nombre ?: "N/A",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            // Fechas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Entrega:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = pedido.entrega,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Column {
                    Text(
                        text = "Recibido:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = pedido.recibido,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun SuccessMessagePedido(message: String) {
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
fun ErrorMessagePedido(message: String) {
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
fun PedidoItemPreview() {
    MaterialTheme {
        PedidoItem(
            pedido = PedidosDto(
                pedidoId = 1,
                clienteId = "CLI-001",
                productosOrdenados = listOf(),
                entrega = "2024-01-15",
                recibido = "2024-01-10",
                montoTotal = 150.75f,
                estadoId = 1,
                estados = ucne.edu.carniceriarosario.data.remote.dto.EstadosDto(1, "Pendiente")
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}