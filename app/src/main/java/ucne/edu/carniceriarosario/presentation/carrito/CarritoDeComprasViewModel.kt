package ucne.edu.carniceriarosario.presentation.carrito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.CarritoDeComprasDto
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import ucne.edu.carniceriarosario.data.repository.CarritoRepository



@HiltViewModel
class CarritoDeComprasViewModel @Inject constructor(
    private val repository: CarritoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CarritoDeComprasUiState())
    val uiState: StateFlow<CarritoDeComprasUiState> = _uiState.asStateFlow()

    init {
        loadCarritos()
    }

    fun loadCarritos() {
        viewModelScope.launch {
            repository.getCarritos().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                carritos = resource.data,
                                carritosFiltrados = resource.data,
                                isLoadingCarritos = false,
                                errorCarritos = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingCarritos = false,
                                errorCarritos = resource.message,
                                carritos = emptyList(),
                                carritosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingCarritos = true, errorCarritos = null)
                        }
                    }
                }
            }
        }
    }

    fun createCarrito() {
        val currentState = _uiState.value

        if (currentState.clienteId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El ID del cliente es requerido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoCarrito = CarritoDeComprasDto(
                carritoId = 0,
                clienteId = currentState.clienteId,
                productos = currentState.productos,
                montoTotal = currentState.montoTotal,
                compra = currentState.compra
            )

            when (val result = repository.createCarrito(nuevoCarrito)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Carrito creado exitosamente",
                            errorMessage = null
                        )
                    }
                    clearForm()
                    loadCarritos()
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
        }
    }

    fun updateCarrito() {
        val currentState = _uiState.value

        if (currentState.clienteId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El ID del cliente es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val carritoActualizado = CarritoDeComprasDto(
                carritoId = currentState.carritoId,
                clienteId = currentState.clienteId,
                productos = currentState.productos,
                montoTotal = currentState.montoTotal,
                compra = currentState.compra
            )

            when (val result = repository.updateCarrito(currentState.carritoId, carritoActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Carrito actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadCarritos()
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

    fun deleteCarrito(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteCarrito(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Carrito eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadCarritos()
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

    // Métodos para manejar productos en el carrito
    fun agregarProducto(producto: DetalleProductosDto) {
        val productosActuales = _uiState.value.productos.toMutableList()
        productosActuales.add(producto)

        val nuevoMontoTotal = calcularMontoTotal(productosActuales)

        _uiState.update {
            it.copy(
                productos = productosActuales,
                montoTotal = nuevoMontoTotal,
                errorMessage = null
            )
        }
    }

    fun eliminarProducto(productoId: Int) {
        val productosActuales = _uiState.value.productos.toMutableList()
        productosActuales.removeAll { it.productoId == productoId }

        val nuevoMontoTotal = calcularMontoTotal(productosActuales)

        _uiState.update {
            it.copy(
                productos = productosActuales,
                montoTotal = nuevoMontoTotal,
                errorMessage = null
            )
        }
    }

    fun actualizarCantidadProducto(productoId: Int, nuevaCantidad: Int) {
        val productosActuales = _uiState.value.productos.map { producto ->
            if (producto.productoId == productoId) {
                producto.copy(cantidad = nuevaCantidad)
            } else {
                producto
            }
        }

        val nuevoMontoTotal = calcularMontoTotal(productosActuales)

        _uiState.update {
            it.copy(
                productos = productosActuales,
                montoTotal = nuevoMontoTotal,
                errorMessage = null
            )
        }
    }

    private fun calcularMontoTotal(productos: List<DetalleProductosDto>): Float {
        return productos.sumOf { (it.precio.toDouble() * it.cantidad) }.toFloat()
    }

    fun setClienteId(clienteId: String) {
        _uiState.update { it.copy(clienteId = clienteId, errorMessage = null) }
    }

    fun setCompra(compra: Boolean) {
        _uiState.update { it.copy(compra = compra, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val carritosFiltrados = if (query.isBlank()) {
            _uiState.value.carritos
        } else {
            _uiState.value.carritos.filter { carrito ->
                carrito.clienteId.contains(query, ignoreCase = true) ||
                        carrito.carritoId.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                carritosFiltrados = carritosFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorCarritos = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                carritoId = 0,
                clienteId = "",
                productos = emptyList(),
                montoTotal = 0f,
                compra = false,
                carritoSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setCarritoForEdit(carrito: CarritoDeComprasDto) {
        _uiState.update {
            it.copy(
                carritoId = carrito.carritoId,
                clienteId = carrito.clienteId,
                productos = carrito.productos,
                montoTotal = carrito.montoTotal,
                compra = carrito.compra,
                carritoSeleccionado = carrito,
                errorMessage = null
            )
        }
    }
}