package ucne.edu.carniceriarosario.presentation.estado

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// ✔️ Reemplazando tu antiguo paquete
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.EstadosDto
import ucne.edu.carniceriarosario.data.repository.EstadosRepository

// ✔️ UI State correcto (asegúrate de que lo tengas creado)
import ucne.edu.carniceriarosario.presentation.estado.EstadosUiState


@HiltViewModel
class EstadosViewModel @Inject constructor(
    private val repository: EstadosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstadosUiState())
    val uiState: StateFlow<EstadosUiState> = _uiState.asStateFlow()

    init {
        loadEstados()
    }

    fun loadEstados() {
        viewModelScope.launch {
            repository.getEstados().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                estados = resource.data,
                                estadosFiltrados = resource.data,
                                isLoadingEstados = false,
                                errorEstados = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingEstados = false,
                                errorEstados = resource.message,
                                estados = emptyList(),
                                estadosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingEstados = true, errorEstados = null)
                        }
                    }
                }
            }
        }
    }

    fun createEstado() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoEstado = EstadosDto(
                estadosId = 0,
                nombre = currentState.nombre
            )

            when (val result = repository.createEstado(nuevoEstado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Estado creado exitosamente",
                            errorMessage = null
                        )
                    }
                    clearForm()
                    loadEstados()
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

    fun updateEstado() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val estadoActualizado = EstadosDto(
                estadosId = currentState.estadoId,
                nombre = currentState.nombre
            )

            when (val result = repository.updateEstado(currentState.estadoId, estadoActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Estado actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadEstados()
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

    fun deleteEstado(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteEstado(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Estado eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadEstados()
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

    fun setNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val estadosFiltrados = if (query.isBlank()) {
            _uiState.value.estados
        } else {
            _uiState.value.estados.filter { estado ->
                estado.nombre?.contains(query, ignoreCase = true) == true ||
                        estado.estadosId.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                estadosFiltrados = estadosFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorEstados = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                estadoId = 0,
                nombre = "",
                estadoSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setEstadoForEdit(estado: EstadosDto) {
        _uiState.update {
            it.copy(
                estadoId = estado.estadosId,
                nombre = estado.nombre ?: "",
                estadoSeleccionado = estado,
                errorMessage = null
            )
        }
    }
}