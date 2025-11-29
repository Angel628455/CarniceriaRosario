package ucne.edu.carniceriarosario.presentation.producto

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import kotlin.let

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProductoScreen(
    viewModel: ProductoViewModel = hiltViewModel(),
    productoId: Int? = null,
    onNavigateBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isEditMode = productoId != null && productoId > 0

    LaunchedEffect(productoId) {
        if (isEditMode) {
            viewModel.getProductoById(productoId!!)
        } else {
            viewModel.clearForm()
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
                title = { Text(if (isEditMode) "Editar Producto" else "Nuevo Producto") },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Mensajes
                uiState.errorMessage?.let { ErrorMessage(it) }
                uiState.successMessage?.let { SuccessMessage(it) }

                // Información básica
                SectionHeader("Información Básica")

                OutlinedTextField(
                    value = uiState.nombre,
                    onValueChange = { viewModel.setNombre(it) },
                    label = { Text("Nombre *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.errorMessage?.contains("nombre", ignoreCase = true) == true
                )

                OutlinedTextField(
                    value = uiState.codigo,
                    onValueChange = { viewModel.setCodigo(it) },
                    label = { Text("Código *") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Face, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = uiState.errorMessage?.contains("código", ignoreCase = true) == true
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

                OutlinedTextField(
                    value = uiState.imagenProducto,
                    onValueChange = { viewModel.setImagenProducto(it) },
                    label = { Text("URL Imagen") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Home, contentDescription = null)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("https://...") }
                )

                Divider()

                // Precios
                SectionHeader("Precios y Costos")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.precioLibra,
                        onValueChange = { viewModel.setPrecioLibra(it) },
                        label = { Text("Precio/lb *") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = uiState.errorMessage?.contains("precio", ignoreCase = true) == true
                    )

                    OutlinedTextField(
                        value = uiState.costoLibra,
                        onValueChange = { viewModel.setCostoLibra(it) },
                        label = { Text("Costo/lb *") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = uiState.errorMessage?.contains("costo", ignoreCase = true) == true
                    )
                }

                Divider()

                // Inventario y categoría
                SectionHeader("Inventario y Categoría")

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.stock,
                        onValueChange = { viewModel.setStock(it) },
                        label = { Text("Stock *") },
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Check, contentDescription = null)
                        },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiState.errorMessage?.contains("stock", ignoreCase = true) == true
                    )

                    // Dropdown de categorías
                    var expandedCategoria by remember { mutableStateOf(false) }

                    ExposedDropdownMenuBox(
                        expanded = expandedCategoria,
                        onExpandedChange = { expandedCategoria = !expandedCategoria },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = uiState.categorias.find {
                                it.categoriaCarnesId.toString() == uiState.categoriaCarneId
                            }?.nombre ?: "Seleccionar",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Categoría *") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategoria) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                            isError = uiState.errorMessage?.contains("categoría", ignoreCase = true) == true
                        )

                        ExposedDropdownMenu(
                            expanded = expandedCategoria,
                            onDismissRequest = { expandedCategoria = false }
                        ) {
                            uiState.categorias.forEach { categoria ->
                                DropdownMenuItem(
                                    text = { Text(categoria.nombre) },
                                    onClick = {
                                        viewModel.setCategoriaCarneId(categoria.categoriaCarnesId.toString())
                                        expandedCategoria = false
                                    }
                                )
                            }
                        }
                    }
                }

                Divider()

                // Sección de detalles
                SectionHeader("Detalles del Producto *")

                DetalleForm(
                    nombre = uiState.detalleNombre,
                    onNombreChange = { viewModel.setDetalleNombre(it) },
                    precio = uiState.detallePrecio,
                    onPrecioChange = { viewModel.setDetallePrecio(it) },
                    cantidad = uiState.detalleCantidad,
                    onCantidadChange = { viewModel.setDetalleCantidad(it) },
                    imagen = uiState.detalleImagen,
                    onImagenChange = { viewModel.setDetalleImagen(it) },
                    onAddDetalle = { viewModel.addDetalle() }
                )

                // Lista de detalles agregados
                if (uiState.detalles.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Detalles Agregados",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Badge {
                                    Text("${uiState.detalles.size}")
                                }
                            }

                            uiState.detalles.forEachIndexed { index, detalle ->
                                DetalleItem(
                                    detalle = detalle,
                                    onRemove = { viewModel.removeDetalle(index) }
                                )
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
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Cancelar")
                    }

                    Button(
                        onClick = {
                            if (isEditMode) {
                                viewModel.updateProducto()
                            } else {
                                viewModel.createProducto()
                            }
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

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (uiState.isLoadingProductos || uiState.isLoadingCategorias) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun DetalleForm(
    nombre: String,
    onNombreChange: (String) -> Unit,
    precio: String,
    onPrecioChange: (String) -> Unit,
    cantidad: String,
    onCantidadChange: (String) -> Unit,
    imagen: String,
    onImagenChange: (String) -> Unit,
    onAddDetalle: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Agregar Detalle",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = onNombreChange,
                label = { Text("Nombre del detalle") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = precio,
                    onValueChange = onPrecioChange,
                    label = { Text("Precio") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.AccountBox, contentDescription = null)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )

                OutlinedTextField(
                    value = cantidad,
                    onValueChange = onCantidadChange,
                    label = { Text("Cantidad") },
                    leadingIcon = {
                        Icon(imageVector = Icons.Default.Call, contentDescription = null)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }

            OutlinedTextField(
                value = imagen,
                onValueChange = onImagenChange,
                label = { Text("URL Imagen") },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Face, contentDescription = null)
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("https://...") }
            )

            Button(
                onClick = onAddDetalle,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Agregar Detalle")
            }
        }
    }
}

@Composable
fun DetalleItem(
    detalle: DetalleProductosDto,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
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
                    text = detalle.productos ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Precio: $${String.format("%.2f", detalle.precio)} | Cant: ${detalle.cantidad}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun SuccessMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
fun ErrorMessage(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
fun DetalleFormPreview() {
    MaterialTheme {
        Surface {
            Column(modifier = Modifier.padding(16.dp)) {
                DetalleForm(
                    nombre = "Chuletón",
                    onNombreChange = {},
                    precio = "15.99",
                    onPrecioChange = {},
                    cantidad = "2",
                    onCantidadChange = {},
                    imagen = "",
                    onImagenChange = {},
                    onAddDetalle = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetalleItemPreview() {
    MaterialTheme {
        Surface {
            DetalleItem(
                detalle = DetalleProductosDto(
                    detalleId = 1,
                    productos = "Chuletón Premium",
                    precio = 15.99f,
                    cantidad = 2,
                    imagen = ""
                ),
                onRemove = {}
            )
        }
    }
}