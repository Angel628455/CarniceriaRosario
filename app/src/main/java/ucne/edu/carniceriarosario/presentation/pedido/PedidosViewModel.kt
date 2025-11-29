package ucne.edu.carniceriarosario.presentation.pedido

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.carniceriarosario.data.remote.Resource
import javax.inject.Inject
import ucne.edu.carniceriarosario.data.repository.PedidosRepository
import ucne.edu.carniceriarosario.data.remote.dto.PedidosDto
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto

// UI State
import ucne.edu.carniceriarosario.presentation.pedido.PedidosUiState


@HiltViewModel
class PedidosViewModel @Inject constructor(
    private val repository: PedidosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PedidosUiState())
    val uiState: StateFlow<PedidosUiState> = _uiState.asStateFlow()

    init {
        loadPedidos()
    }

    fun loadPedidos() {
        viewModelScope.launch {
            repository.getPedidos().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                pedidos = resource.data,
                                pedidosFiltrados = resource.data,
                                isLoadingPedidos = false,
                                errorPedidos = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingPedidos = false,
                                errorPedidos = resource.message,
                                pedidos = emptyList(),
                                pedidosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingPedidos = true, errorPedidos = null)
                        }
                    }
                }
            }
        }
    }

    fun createPedido() {
        val currentState = _uiState.value

        if (currentState.clienteId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El ID del cliente es requerido") }
            return
        }

        if (currentState.estadoId <= 0) {
            _uiState.update { it.copy(errorMessage = "El estado es requerido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoPedido = PedidosDto(
                pedidoId = 0,
                clienteId = currentState.clienteId,
                productosOrdenados = currentState.productosOrdenados,
                entrega = currentState.entrega,
                recibido = currentState.recibido,
                montoTotal = currentState.montoTotal,
                estadoId = currentState.estadoId,
                estados = null
            )

            when (val result = repository.createPedido(nuevoPedido)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Pedido creado exitosamente",
                            errorMessage = null
                        )
                    }
                    clearForm()
                    loadPedidos()
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

    fun updatePedido() {
        val currentState = _uiState.value

        if (currentState.clienteId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El ID del cliente es requerido") }
            return
        }

        if (currentState.estadoId <= 0) {
            _uiState.update { it.copy(errorMessage = "El estado es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val pedidoActualizado = PedidosDto(
                pedidoId = currentState.pedidoId,
                clienteId = currentState.clienteId,
                productosOrdenados = currentState.productosOrdenados,
                entrega = currentState.entrega,
                recibido = currentState.recibido,
                montoTotal = currentState.montoTotal,
                estadoId = currentState.estadoId,
                estados = null
            )

            when (val result = repository.updatePedido(currentState.pedidoId, pedidoActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Pedido actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadPedidos()
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

    fun deletePedido(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deletePedido(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Pedido eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadPedidos()
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

    // Métodos para manejar productos en el pedido
    fun agregarProducto(producto: DetalleProductosDto) {
        val productosActuales = _uiState.value.productosOrdenados.toMutableList()
        productosActuales.add(producto)

        val nuevoMontoTotal = calcularMontoTotal(productosActuales)

        _uiState.update {
            it.copy(
                productosOrdenados = productosActuales,
                montoTotal = nuevoMontoTotal,
                errorMessage = null
            )
        }
    }

    fun eliminarProducto(productoId: Int) {
        val productosActuales = _uiState.value.productosOrdenados.toMutableList()
        productosActuales.removeAll { it.productoId == productoId }

        val nuevoMontoTotal = calcularMontoTotal(productosActuales)

        _uiState.update {
            it.copy(
                productosOrdenados = productosActuales,
                montoTotal = nuevoMontoTotal,
                errorMessage = null
            )
        }
    }

    fun actualizarCantidadProducto(productoId: Int, nuevaCantidad: Int) {
        val productosActuales = _uiState.value.productosOrdenados.map { producto ->
            if (producto.productoId == productoId) {
                producto.copy(cantidad = nuevaCantidad)
            } else {
                producto
            }
        }

        val nuevoMontoTotal = calcularMontoTotal(productosActuales)

        _uiState.update {
            it.copy(
                productosOrdenados = productosActuales,
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

    fun setEntrega(entrega: String) {
        _uiState.update { it.copy(entrega = entrega, errorMessage = null) }
    }

    fun setRecibido(recibido: String) {
        _uiState.update { it.copy(recibido = recibido, errorMessage = null) }
    }

    fun setEstadoId(estadoId: Int) {
        _uiState.update { it.copy(estadoId = estadoId, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val pedidosFiltrados = if (query.isBlank()) {
            _uiState.value.pedidos
        } else {
            _uiState.value.pedidos.filter { pedido ->
                pedido.clienteId?.contains(query, ignoreCase = true) == true ||
                        pedido.pedidoId.toString().contains(query) ||
                        pedido.estados?.nombre?.contains(query, ignoreCase = true) == true
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                pedidosFiltrados = pedidosFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorPedidos = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                pedidoId = 0,
                clienteId = "",
                productosOrdenados = emptyList(),
                entrega = "",
                recibido = "",
                montoTotal = 0f,
                estadoId = 0,
                pedidoSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setPedidoForEdit(pedido: PedidosDto) {
        _uiState.update {
            it.copy(
                pedidoId = pedido.pedidoId,
                clienteId = pedido.clienteId ?: "",
                productosOrdenados = pedido.productosOrdenados,
                entrega = pedido.entrega,
                recibido = pedido.recibido,
                montoTotal = pedido.montoTotal,
                estadoId = pedido.estadoId,
                pedidoSeleccionado = pedido,
                errorMessage = null
            )
        }
    }
}