package ucne.edu.carniceriarosario.presentation.producto

import ucne.edu.carniceriarosario.data.repository.CategoriaRepository
import ucne.edu.carniceriarosario.data.repository.ProductoRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import ucne.edu.carniceriarosario.data.remote.dto.ProductosDto
import ucne.edu.carniceriarosario.data.repository.DetalleProductosRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProductoViewModel @Inject constructor(
    private val repository: ProductoRepository,
    private val categoriaRepository: CategoriaRepository,
    private val detalleRepository: DetalleProductosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductoUiState())
    val uiState: StateFlow<ProductoUiState> = _uiState.asStateFlow()

    init {
        loadProductos()
        loadCategorias()
    }

    fun loadCategorias() {
        viewModelScope.launch {
            categoriaRepository.getCategorias().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                categorias = resource.data,
                                isLoadingCategorias = false
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoadingCategorias = true) }
                    }
                    else -> {
                        _uiState.update { it.copy(isLoadingCategorias = false) }
                    }
                }
            }
        }
    }

    fun loadProductos() {
        viewModelScope.launch {
            repository.getProductos().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                productos = resource.data,
                                productosFiltrados = resource.data,
                                isLoadingProductos = false,
                                errorProductos = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingProductos = false,
                                errorProductos = resource.message,
                                productos = emptyList(),
                                productosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingProductos = true, errorProductos = null)
                        }
                    }
                }
            }
        }
    }

    fun getProductoById(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingProductos = true) }

            when (val result = repository.getProducto(id)) {
                is Resource.Success -> {
                    val producto = result.data
                    _uiState.update {
                        it.copy(
                            productoSeleccionado = producto,
                            productoId = producto.productoId,
                            nombre = producto.nombre ?: "",
                            imagenProducto = producto.imagenProducto ?: "",
                            codigo = producto.codigo ?: "",
                            descripcion = producto.descripcion ?: "",
                            precioLibra = producto.precioLibra.toString(),
                            costoLibra = producto.costoLibra.toString(),
                            stock = producto.stock.toString(),
                            categoriaCarneId = producto.categoriaCarneId.toString(),
                            isLoadingProductos = false,
                            errorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingProductos = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    // Gestión de detalles
    fun addDetalle() {
        val currentState = _uiState.value

        if (currentState.detalleNombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre del detalle es requerido") }
            return
        }

        val precio = currentState.detallePrecio.toFloatOrNull()
        if (precio == null || precio <= 0) {
            _uiState.update { it.copy(errorMessage = "El precio del detalle debe ser mayor a 0") }
            return
        }

        val cantidad = currentState.detalleCantidad.toIntOrNull()
        if (cantidad == null || cantidad <= 0) {
            _uiState.update { it.copy(errorMessage = "La cantidad debe ser mayor a 0") }
            return
        }

        val nuevoDetalle = DetalleProductosDto(
            detalleId = 0,
            productoId = 0, // Se asignará después de crear el producto
            productos = currentState.detalleNombre,
            carritoId = 0,
            precio = precio,
            cantidad = cantidad,
            imagen = currentState.detalleImagen.ifBlank { null }
        )

        _uiState.update {
            it.copy(
                detalles = it.detalles + nuevoDetalle,
                detalleNombre = "",
                detallePrecio = "",
                detalleCantidad = "",
                detalleImagen = "",
                errorMessage = null,
                successMessage = "Detalle agregado"
            )
        }

        // Limpiar mensaje de éxito después de un tiempo
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _uiState.update { it.copy(successMessage = null) }
        }
    }

    fun removeDetalle(index: Int) {
        _uiState.update {
            it.copy(
                detalles = it.detalles.filterIndexed { i, _ -> i != index }
            )
        }
    }

    fun createProducto() {
        val currentState = _uiState.value

        // Validaciones
        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        if (currentState.codigo.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El código es requerido") }
            return
        }

        val precioLibra = currentState.precioLibra.toFloatOrNull()
        if (precioLibra == null || precioLibra <= 0) {
            _uiState.update { it.copy(errorMessage = "El precio debe ser mayor a 0") }
            return
        }

        val costoLibra = currentState.costoLibra.toFloatOrNull()
        if (costoLibra == null || costoLibra <= 0) {
            _uiState.update { it.copy(errorMessage = "El costo debe ser mayor a 0") }
            return
        }

        val stock = currentState.stock.toIntOrNull()
        if (stock == null || stock < 0) {
            _uiState.update { it.copy(errorMessage = "El stock debe ser mayor o igual a 0") }
            return
        }

        val categoriaId = currentState.categoriaCarneId.toIntOrNull()
        if (categoriaId == null || categoriaId <= 0) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar una categoría válida") }
            return
        }

        if (currentState.detalles.isEmpty()) {
            _uiState.update { it.copy(errorMessage = "Debe agregar al menos un detalle al producto") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            try {
                val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

                val nuevoProducto = ProductosDto(
                    productoId = 0,
                    nombre = currentState.nombre,
                    imagenProducto = currentState.imagenProducto.ifBlank { null },
                    codigo = currentState.codigo,
                    descripcion = currentState.descripcion.ifBlank { null },
                    precioLibra = precioLibra,
                    costoLibra = costoLibra,
                    stock = stock,
                    fechaIngreso = fechaActual,
                    categoriaCarneId = categoriaId
                )

                when (val result = repository.createProducto(nuevoProducto)) {
                    is Resource.Success -> {
                        val productoCreado = result.data
                        var detallesCreados = 0
                        var errorEnDetalle = false

                        // Crear los detalles asociados al producto
                        for (detalle in currentState.detalles) {
                            val detalleConProductoId = detalle.copy(
                                productoId = productoCreado.productoId
                            )

                            when (detalleRepository.createDetalleProducto(detalleConProductoId)) {
                                is Resource.Success -> {
                                    detallesCreados++
                                }
                                is Resource.Error -> {
                                    errorEnDetalle = true
                                }
                                else -> {}
                            }
                        }

                        val mensaje = if (errorEnDetalle) {
                            "Producto creado con $detallesCreados de ${currentState.detalles.size} detalles"
                        } else {
                            "Producto creado exitosamente con ${currentState.detalles.size} detalles"
                        }

                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = mensaje,
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadProductos()
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                errorMessage = result.message,
                                successMessage = null
                            )
                        }
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCreating = false,
                        errorMessage = e.message ?: "Error al crear producto",
                        successMessage = null
                    )
                }
            }
        }
    }

    fun updateProducto() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        if (currentState.codigo.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El código es requerido") }
            return
        }

        val precioLibra = currentState.precioLibra.toFloatOrNull()
        if (precioLibra == null || precioLibra <= 0) {
            _uiState.update { it.copy(errorMessage = "El precio debe ser mayor a 0") }
            return
        }

        val costoLibra = currentState.costoLibra.toFloatOrNull()
        if (costoLibra == null || costoLibra <= 0) {
            _uiState.update { it.copy(errorMessage = "El costo debe ser mayor a 0") }
            return
        }

        val stock = currentState.stock.toIntOrNull()
        if (stock == null || stock < 0) {
            _uiState.update { it.copy(errorMessage = "El stock debe ser mayor o igual a 0") }
            return
        }

        val categoriaId = currentState.categoriaCarneId.toIntOrNull()
        if (categoriaId == null || categoriaId <= 0) {
            _uiState.update { it.copy(errorMessage = "Debe seleccionar una categoría válida") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val productoActualizado = ProductosDto(
                productoId = currentState.productoId,
                nombre = currentState.nombre,
                imagenProducto = currentState.imagenProducto.ifBlank { null },
                codigo = currentState.codigo,
                descripcion = currentState.descripcion.ifBlank { null },
                precioLibra = precioLibra,
                costoLibra = costoLibra,
                stock = stock,
                fechaIngreso = currentState.productoSeleccionado?.fechaIngreso ?: "",
                categoriaCarneId = categoriaId
            )

            when (val result = repository.updateProducto(currentState.productoId, productoActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Producto actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadProductos()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = result.message,
                            successMessage = null
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun deleteProducto(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteProducto(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Producto eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadProductos()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            errorMessage = result.message,
                            successMessage = null
                        )
                    }
                }
                else -> {}
            }
        }
    }

    // Setters
    fun setNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errorMessage = null) }
    }

    fun setImagenProducto(imagenProducto: String) {
        _uiState.update { it.copy(imagenProducto = imagenProducto, errorMessage = null) }
    }

    fun setCodigo(codigo: String) {
        _uiState.update { it.copy(codigo = codigo, errorMessage = null) }
    }

    fun setDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, errorMessage = null) }
    }

    fun setPrecioLibra(precioLibra: String) {
        _uiState.update { it.copy(precioLibra = precioLibra, errorMessage = null) }
    }

    fun setCostoLibra(costoLibra: String) {
        _uiState.update { it.copy(costoLibra = costoLibra, errorMessage = null) }
    }

    fun setStock(stock: String) {
        _uiState.update { it.copy(stock = stock, errorMessage = null) }
    }

    fun setCategoriaCarneId(categoriaCarneId: String) {
        _uiState.update { it.copy(categoriaCarneId = categoriaCarneId, errorMessage = null) }
    }

    // Setters para detalles
    fun setDetalleNombre(nombre: String) {
        _uiState.update { it.copy(detalleNombre = nombre, errorMessage = null) }
    }

    fun setDetallePrecio(precio: String) {
        _uiState.update { it.copy(detallePrecio = precio, errorMessage = null) }
    }

    fun setDetalleCantidad(cantidad: String) {
        _uiState.update { it.copy(detalleCantidad = cantidad, errorMessage = null) }
    }

    fun setDetalleImagen(imagen: String) {
        _uiState.update { it.copy(detalleImagen = imagen, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val productosFiltrados = if (query.isBlank()) {
            _uiState.value.productos
        } else {
            _uiState.value.productos.filter { producto ->
                producto.nombre?.contains(query, ignoreCase = true) == true ||
                        producto.codigo?.contains(query, ignoreCase = true) == true ||
                        producto.descripcion?.contains(query, ignoreCase = true) == true ||
                        producto.productoId.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                productosFiltrados = productosFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorProductos = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                productoId = 0,
                nombre = "",
                imagenProducto = "",
                codigo = "",
                descripcion = "",
                precioLibra = "",
                costoLibra = "",
                stock = "",
                categoriaCarneId = "",
                detalles = emptyList(),
                detalleNombre = "",
                detallePrecio = "",
                detalleCantidad = "",
                detalleImagen = "",
                productoSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setProductoForEdit(producto: ProductosDto) {
        _uiState.update {
            it.copy(
                productoId = producto.productoId,
                nombre = producto.nombre ?: "",
                imagenProducto = producto.imagenProducto ?: "",
                codigo = producto.codigo ?: "",
                descripcion = producto.descripcion ?: "",
                precioLibra = producto.precioLibra.toString(),
                costoLibra = producto.costoLibra.toString(),
                stock = producto.stock.toString(),
                categoriaCarneId = producto.categoriaCarneId.toString(),
                productoSeleccionado = producto,
                errorMessage = null
            )
        }
    }
}