package ucne.edu.carniceriarosario.presentation.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
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

// Asegúrate de que existan estos modelos en tu proyecto
import ucne.edu.carniceriarosario.presentation.pago.PagosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePagoScreen(
    viewModel: PagosViewModel = hiltViewModel(),
    pagoId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = pagoId != null && pagoId > 0

    // Cargar pago para edición
    LaunchedEffect(pagoId) {
        if (isEditMode) {
            val pago = uiState.pagos.find { it.pagoId == pagoId }
            pago?.let { viewModel.setPagoForEdit(it) }
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
                title = { Text(if (isEditMode) "Editar Pago y Detalles" else "Nuevo Pago y Detalles") },
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
            uiState.errorMessage?.let { ErrorMessagePago(it) }
            uiState.successMessage?.let { SuccessMessagePago(it) }

            // Sección de información del pago
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Información del Pago",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))

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

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Monto Pagado
                    OutlinedTextField(
                        value = if (uiState.montoPagado > 0f) uiState.montoPagado.toString() else "",
                        onValueChange = {
                            val monto = it.toFloatOrNull() ?: 0f
                            viewModel.setMontoPagado(monto)
                        },
                        label = { Text("Monto Pagado *") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Send, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = uiState.errorMessage?.contains("monto", ignoreCase = true) == true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Campo Fecha de Pago
                    OutlinedTextField(
                        value = uiState.fechaPago,
                        onValueChange = { viewModel.setFechaPago(it) },
                        label = { Text("Fecha de Pago") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        placeholder = { Text("YYYY-MM-DD") }
                    )
                }
            }

            // Sección de detalles del pago
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
                            text = "Detalles del Pago",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.detallesPagoTemporal.size} detalles",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Formulario para agregar detalle
                    AgregarDetallePagoForm(
                        onAgregarDetalle = { detalle ->
                            viewModel.agregarDetallePago(detalle)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Lista de detalles temporales
                    if (uiState.detallesPagoTemporal.isEmpty()) {
                        Text(
                            text = "No hay detalles agregados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            uiState.detallesPagoTemporal.forEachIndexed { index, detalle ->
                                DetallePagoTemporalItem(
                                    detalle = detalle,
                                    index = index,
                                    onEliminar = { viewModel.eliminarDetallePago(index) }
                                )
                            }
                        }
                    }
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
                        if (isEditMode) viewModel.updatePago()
                        else viewModel.createPago()
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isCreating && !uiState.isUpdating &&
                            uiState.clienteId.isNotBlank() && uiState.montoPagado > 0f
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
fun AgregarDetallePagoForm(onAgregarDetalle: (DetallePagoTemporal) -> Unit) {
    var pedidoId by remember { mutableStateOf("") }
    var metodoPagoId by remember { mutableStateOf("") }
    var montoPagado by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = "Agregar Detalle",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = pedidoId,
                    onValueChange = { pedidoId = it },
                    label = { Text("Pedido ID") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = metodoPagoId,
                    onValueChange = { metodoPagoId = it },
                    label = { Text("Método Pago ID") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = montoPagado,
                onValueChange = { montoPagado = it },
                label = { Text("Monto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    val detalle = DetallePagoTemporal(
                        pedidoId = pedidoId.toIntOrNull() ?: 0,
                        metodoPagoId = metodoPagoId.toIntOrNull() ?: 0,
                        montoPagado = montoPagado.toFloatOrNull() ?: 0f
                    )
                    if (detalle.pedidoId > 0 && detalle.metodoPagoId > 0 && detalle.montoPagado > 0f) {
                        onAgregarDetalle(detalle)
                        pedidoId = ""
                        metodoPagoId = ""
                        montoPagado = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar Detalle")
            }
        }
    }
}

@Composable
fun DetallePagoTemporalItem(
    detalle: DetallePagoTemporal,
    index: Int,
    onEliminar: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
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

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "$${detalle.montoPagado}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreatePagoScreenPreview() {
    MaterialTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Información del Pago",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        // ... campos del formulario
                    }
                }
            }
        }
    }
}