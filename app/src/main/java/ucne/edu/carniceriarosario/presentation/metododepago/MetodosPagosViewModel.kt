package ucne.edu.carniceriarosario.presentation.metododepago

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ✔️ Tus clases reales (ajusta estas rutas si tu estructura cambia)
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.MetodosPagosDto
import ucne.edu.carniceriarosario.data.repository.MetodosPagosRepository
import ucne.edu.carniceriarosario.presentation.metododepago.MetodosPagosUiState


@HiltViewModel
class MetodosPagosViewModel @Inject constructor(
    private val repository: MetodosPagosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MetodosPagosUiState())
    val uiState: StateFlow<MetodosPagosUiState> = _uiState.asStateFlow()

    init {
        loadMetodosPago()
    }

    fun loadMetodosPago() {
        viewModelScope.launch {
            repository.getMetodosPago().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                metodosPagos = resource.data,
                                metodosPagosFiltrados = resource.data,
                                isLoadingMetodosPagos = false,
                                errorMetodosPagos = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingMetodosPagos = false,
                                errorMetodosPagos = resource.message,
                                metodosPagos = emptyList(),
                                metodosPagosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingMetodosPagos = true, errorMetodosPagos = null)
                        }
                    }
                }
            }
        }
    }

    fun createMetodoPago() {
        val currentState = _uiState.value

        if (currentState.descripcion.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La descripción es requerida") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoMetodoPago = MetodosPagosDto(
                metodoPagoId = 0,
                descripcion = currentState.descripcion
            )

            when (val result = repository.createMetodoPago(nuevoMetodoPago)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Método de pago creado exitosamente",
                            errorMessage = null
                        )
                    }
                    clearForm()
                    loadMetodosPago()
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

    fun updateMetodoPago() {
        val currentState = _uiState.value

        if (currentState.descripcion.isBlank()) {
            _uiState.update { it.copy(errorMessage = "La descripción es requerida") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val metodoPagoActualizado = MetodosPagosDto(
                metodoPagoId = currentState.metodoPagoId,
                descripcion = currentState.descripcion
            )

            when (val result = repository.updateMetodoPago(currentState.metodoPagoId, metodoPagoActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Método de pago actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadMetodosPago()
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

    fun deleteMetodoPago(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteMetodoPago(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Método de pago eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadMetodosPago()
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

    fun setDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val metodosPagosFiltrados = if (query.isBlank()) {
            _uiState.value.metodosPagos
        } else {
            _uiState.value.metodosPagos.filter { metodoPago ->
                metodoPago.descripcion.contains(query, ignoreCase = true) ||
                        metodoPago.metodoPagoId.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                metodosPagosFiltrados = metodosPagosFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorMetodosPagos = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                metodoPagoId = 0,
                descripcion = "",
                metodoPagoSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setMetodoPagoForEdit(metodoPago: MetodosPagosDto) {
        _uiState.update {
            it.copy(
                metodoPagoId = metodoPago.metodoPagoId,
                descripcion = metodoPago.descripcion,
                metodoPagoSeleccionado = metodoPago,
                errorMessage = null
            )
        }
    }
}