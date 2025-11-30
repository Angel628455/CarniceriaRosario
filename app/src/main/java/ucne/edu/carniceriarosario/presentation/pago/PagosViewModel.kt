package ucne.edu.carniceriarosario.presentation.pago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto
import ucne.edu.carniceriarosario.data.repository.DetallePagosRepository
import ucne.edu.carniceriarosario.data.repository.PagosRepository
import javax.inject.Inject




@HiltViewModel
class PagosViewModel @Inject constructor(
    private val pagosRepository: PagosRepository,
    private val detallesPagosRepository: DetallePagosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PagosUiState())
    val uiState: StateFlow<PagosUiState> = _uiState.asStateFlow()

    init {
        loadPagos()
    }

    fun loadPagos() {
        viewModelScope.launch {
            pagosRepository.getPagos().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                pagos = resource.data,
                                pagosFiltrados = resource.data,
                                isLoadingPagos = false,
                                errorPagos = null
                            )
                        }
                        // Cargar detalles para cada pago
                        resource.data.forEach { pago ->
                            loadDetallesPagos(pago.pagoId)
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingPagos = false,
                                errorPagos = resource.message,
                                pagos = emptyList(),
                                pagosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingPagos = true, errorPagos = null)
                        }
                    }
                }
            }
        }
    }

    private fun loadDetallesPagos(pagoId: Int) {
        viewModelScope.launch {
            detallesPagosRepository.getDetallesPagos().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        val detallesFiltrados = resource.data.filter { it.pagoId == pagoId }
                        _uiState.update { state ->
                            val detallesActualizados = state.detallesPagos.toMutableList()
                            detallesActualizados.removeAll { it.pagoId == pagoId }
                            detallesActualizados.addAll(detallesFiltrados)
                            state.copy(
                                detallesPagos = detallesActualizados,
                                isLoadingDetallesPagos = false,
                                errorDetallesPagos = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingDetallesPagos = false,
                                errorDetallesPagos = resource.message
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingDetallesPagos = true, errorDetallesPagos = null)
                        }
                    }
                }
            }
        }
    }

    fun createPago() {
        val currentState = _uiState.value

        if (currentState.clienteId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El ID del cliente es requerido") }
            return
        }

        if (currentState.montoPagado <= 0f) {
            _uiState.update { it.copy(errorMessage = "El monto debe ser mayor a 0") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoPago = PagosDto(
                pagoId = 0,
                clienteId = currentState.clienteId,
                montoPagado = currentState.montoPagado,
                fechaPago = currentState.fechaPago,
                detallesPagos = emptyList()
            )

            when (val result = pagosRepository.createPago(nuevoPago)) {
                is Resource.Success -> {
                    // Crear detalles de pago si existen
                    if (currentState.detallesPagoTemporal.isNotEmpty()) {
                        crearDetallesPago(result.data.pagoId, currentState.detallesPagoTemporal)
                    } else {
                        _uiState.update {
                            it.copy(
                                isCreating = false,
                                successMessage = "Pago creado exitosamente",
                                errorMessage = null
                            )
                        }
                        clearForm()
                        loadPagos()
                    }
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

    private suspend fun crearDetallesPago(pagoId: Int, detalles: List<DetallePagoTemporal>) {
        for (detalle in detalles) {
            val nuevoDetalle = DetallesPagosDto(
                detallePagoId = 0,
                pedidoId = detalle.pedidoId,
                pagoId = pagoId,
                metodoPagoId = detalle.metodoPagoId,
                montoPagado = detalle.montoPagado
            )

            when (val result = detallesPagosRepository.createDetallePago(nuevoDetalle)) {
                is Resource.Success -> {
                    // Detalle creado exitosamente
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = "Error al crear detalle: ${result.message}",
                            successMessage = null
                        )
                    }
                    return
                }
                else -> {}
            }
        }

        _uiState.update {
            it.copy(
                isCreating = false,
                successMessage = "Pago y detalles creados exitosamente",
                errorMessage = null
            )
        }
        clearForm()
        loadPagos()
    }

    fun updatePago() {
        val currentState = _uiState.value

        if (currentState.clienteId.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El ID del cliente es requerido") }
            return
        }

        if (currentState.montoPagado <= 0f) {
            _uiState.update { it.copy(errorMessage = "El monto debe ser mayor a 0") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val pagoActualizado = PagosDto(
                pagoId = currentState.pagoId,
                clienteId = currentState.clienteId,
                montoPagado = currentState.montoPagado,
                fechaPago = currentState.fechaPago,
                detallesPagos = emptyList()
            )

            when (val result = pagosRepository.updatePago(currentState.pagoId, pagoActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Pago actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadPagos()
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

    fun deletePago(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            // Primero eliminar los detalles de pago
            val detallesAEliminar = _uiState.value.detallesPagos.filter { it.pagoId == id }
            for (detalle in detallesAEliminar) {
                detallesPagosRepository.deleteDetallePago(detalle.detallePagoId)
            }

            // Luego eliminar el pago
            when (val result = pagosRepository.deletePago(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Pago y detalles eliminados exitosamente",
                            errorMessage = null
                        )
                    }
                    loadPagos()
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

    // Métodos para detalles de pago
    fun agregarDetallePago(detalle: DetallePagoTemporal) {
        val detallesActuales = _uiState.value.detallesPagoTemporal.toMutableList()
        detallesActuales.add(detalle)
        _uiState.update {
            it.copy(
                detallesPagoTemporal = detallesActuales,
                errorMessage = null
            )
        }
    }

    fun eliminarDetallePago(index: Int) {
        val detallesActuales = _uiState.value.detallesPagoTemporal.toMutableList()
        if (index in detallesActuales.indices) {
            detallesActuales.removeAt(index)
            _uiState.update {
                it.copy(
                    detallesPagoTemporal = detallesActuales,
                    errorMessage = null
                )
            }
        }
    }

    fun setClienteId(clienteId: String) {
        _uiState.update { it.copy(clienteId = clienteId, errorMessage = null) }
    }

    fun setMontoPagado(montoPagado: Float) {
        _uiState.update { it.copy(montoPagado = montoPagado, errorMessage = null) }
    }

    fun setFechaPago(fechaPago: String) {
        _uiState.update { it.copy(fechaPago = fechaPago, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val pagosFiltrados = if (query.isBlank()) {
            _uiState.value.pagos
        } else {
            _uiState.value.pagos.filter { pago ->
                pago.clienteId.contains(query, ignoreCase = true) ||
                        pago.pagoId.toString().contains(query) ||
                        pago.montoPagado.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                pagosFiltrados = pagosFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorPagos = null,
                errorDetallesPagos = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                pagoId = 0,
                clienteId = "",
                montoPagado = 0f,
                fechaPago = "",
                detallesPagoTemporal = emptyList(),
                pagoSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setPagoForEdit(pago: PagosDto) {
        _uiState.update {
            it.copy(
                pagoId = pago.pagoId,
                clienteId = pago.clienteId,
                montoPagado = pago.montoPagado,
                fechaPago = pago.fechaPago,
                pagoSeleccionado = pago,
                errorMessage = null
            )
        }
        // Cargar detalles del pago seleccionado
        loadDetallesPagos(pago.pagoId)
    }
}

data class DetallePagoTemporal(
    val pedidoId: Int = 0,
    val metodoPagoId: Int = 0,
    val montoPagado: Float = 0f
)