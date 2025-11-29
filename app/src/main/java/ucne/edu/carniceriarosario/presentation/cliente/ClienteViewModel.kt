package ucne.edu.carniceriarosario.presentation.cliente

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
import ucne.edu.carniceriarosario.data.remote.dto.ClienteDto
import ucne.edu.carniceriarosario.data.repository.ClienteRepository
import ucne.edu.carniceriarosario.presentation.cliente.ClienteUiState


@HiltViewModel
class ClienteViewModel @Inject constructor(
    private val repository: ClienteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ClienteUiState())
    val uiState: StateFlow<ClienteUiState> = _uiState.asStateFlow()

    init {
        loadClientes()
    }

    fun loadClientes() {
        viewModelScope.launch {
            repository.getClientes().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                clientes = resource.data,
                                clientesFiltrados = resource.data,
                                isLoadingClientes = false,
                                errorClientes = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingClientes = false,
                                errorClientes = resource.message,
                                clientes = emptyList(),
                                clientesFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingClientes = true, errorClientes = null)
                        }
                    }
                }
            }
        }
    }

    fun createCliente() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoCliente = ClienteDto(
                clienteId = 0,
                nombre = currentState.nombre,
                apellidos = currentState.apellidos,
                telefono = currentState.telefono
            )

            when (val result = repository.createCliente(nuevoCliente)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Cliente creado exitosamente",
                            errorMessage = null
                        )
                    }
                    clearForm()
                    loadClientes()
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

    fun updateCliente() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val clienteActualizado = ClienteDto(
                clienteId = currentState.clienteId,
                nombre = currentState.nombre,
                apellidos = currentState.apellidos,
                telefono = currentState.telefono
            )

            when (val result = repository.updateCliente(currentState.clienteId, clienteActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Cliente actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadClientes()
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

    fun deleteCliente(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteCliente(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Cliente eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadClientes()
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

    fun setApellidos(apellidos: String) {
        _uiState.update { it.copy(apellidos = apellidos, errorMessage = null) }
    }

    fun setTelefono(telefono: String) {
        _uiState.update { it.copy(telefono = telefono, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val clientesFiltrados = if (query.isBlank()) {
            _uiState.value.clientes
        } else {
            _uiState.value.clientes.filter { cliente ->
                cliente.nombre.contains(query, ignoreCase = true) ||
                        cliente.apellidos.contains(query, ignoreCase = true) ||
                        cliente.telefono.contains(query, ignoreCase = true) ||
                        cliente.clienteId.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                clientesFiltrados = clientesFiltrados
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorClientes = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                clienteId = 0,
                nombre = "",
                apellidos = "",
                telefono = "",
                clienteSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setClienteForEdit(cliente: ClienteDto) {
        _uiState.update {
            it.copy(
                clienteId = cliente.clienteId,
                nombre = cliente.nombre,
                apellidos = cliente.apellidos,
                telefono = cliente.telefono,
                clienteSeleccionado = cliente,
                errorMessage = null
            )
        }
    }
}