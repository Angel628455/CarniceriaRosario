package ucne.edu.carniceriarosario.presentation.usuario

import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.UsuarioDto
import ucne.edu.carniceriarosario.data.repository.UsuarioRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsuarioViewModel @Inject constructor(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UsuarioUiState())
    val uiState: StateFlow<UsuarioUiState> = _uiState.asStateFlow()

    private val _usuarioState = MutableStateFlow<Resource<UsuarioDto>?>(null)
    val usuarioState: StateFlow<Resource<UsuarioDto>?> = _usuarioState.asStateFlow()

    private val _usuariosState = MutableStateFlow<Resource<List<UsuarioDto>>>(Resource.Loading())
    val usuariosState: StateFlow<Resource<List<UsuarioDto>>> = _usuariosState.asStateFlow()

    init {
        loadUsuarios()
    }

    fun loadUsuarios() {
        viewModelScope.launch {
            repository.getUsuarios().collect { resource ->
                _usuariosState.value = resource
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                usuarios = resource.data,
                                usuariosFiltrados = resource.data,
                                isLoadingUsuarios = false,
                                errorUsuarios = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingUsuarios = false,
                                errorUsuarios = resource.message,
                                usuarios = emptyList(),
                                usuariosFiltrados = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingUsuarios = true, errorUsuarios = null)
                        }
                    }
                }
            }
        }
    }

    fun getUsuarioById(id: Int) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingUsuarios = true) }

            when (val result = repository.getUsuario(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            usuarioSeleccionado = result.data,
                            usuarioId = result.data.usuarioId,
                            userName = result.data.userName,
                            password = result.data.password,
                            isLoadingUsuarios = false,
                            errorMessage = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoadingUsuarios = false,
                            errorMessage = result.message
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun createUsuario(usuario: UsuarioDto) {
        viewModelScope.launch {
            _usuarioState.value = Resource.Loading()
            val result = repository.createUsuario(usuario)
            _usuarioState.value = result

            when (result) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            successMessage = "Usuario creado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadUsuarios()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.message,
                            successMessage = null
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun createUsuario() {
        val currentState = _uiState.value

        if (currentState.userName.isBlank() || currentState.password.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Todos los campos son requeridos")
            }
            return
        }

        if (currentState.password != currentState.confirmarPassword) {
            _uiState.update {
                it.copy(errorMessage = "Las contraseñas no coinciden")
            }
            return
        }

        if (currentState.password.length < 4) {
            _uiState.update {
                it.copy(errorMessage = "La contraseña debe tener al menos 4 caracteres")
            }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevoUsuario = UsuarioDto(
                usuarioId = 0,
                userName = currentState.userName,
                password = currentState.password
            )

            when (val result = repository.createUsuario(nuevoUsuario)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Usuario creado exitosamente",
                            errorMessage = null,
                            userName = "",
                            password = "",
                            confirmarPassword = ""
                        )
                    }
                    loadUsuarios()
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

    fun updateUsuario() {
        val currentState = _uiState.value

        if (currentState.userName.isBlank() || currentState.password.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Todos los campos son requeridos")
            }
            return
        }

        if (currentState.password.length < 4) {
            _uiState.update {
                it.copy(errorMessage = "La contraseña debe tener al menos 4 caracteres")
            }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val usuarioActualizado = UsuarioDto(
                usuarioId = currentState.usuarioId,
                userName = currentState.userName,
                password = currentState.password
            )

            when (val result = repository.updateUsuario(currentState.usuarioId, usuarioActualizado)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Usuario actualizado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadUsuarios()
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

    fun deleteUsuario(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteUsuario(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Usuario eliminado exitosamente",
                            errorMessage = null
                        )
                    }
                    loadUsuarios()
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

    fun setUserName(userName: String) {
        _uiState.update { it.copy(userName = userName, errorMessage = null) }
    }

    fun setPassword(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    fun setConfirmarPassword(confirmarPassword: String) {
        _uiState.update { it.copy(confirmarPassword = confirmarPassword, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val usuariosFiltrados = if (query.isBlank()) {
            _uiState.value.usuarios
        } else {
            _uiState.value.usuarios.filter { usuario ->
                usuario.userName.contains(query, ignoreCase = true) ||
                        usuario.usuarioId.toString().contains(query)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                usuariosFiltrados = usuariosFiltrados
            )
        }
    }

    fun togglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun toggleConfirmarPasswordVisibility() {
        _uiState.update { it.copy(confirmarPasswordVisible = !it.confirmarPasswordVisible) }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorUsuarios = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                usuarioId = 0,
                userName = "",
                password = "",
                confirmarPassword = "",
                usuarioSeleccionado = null,
                errorMessage = null
            )
        }
    }

    fun setUsuarioForEdit(usuario: UsuarioDto) {
        _uiState.update {
            it.copy(
                usuarioId = usuario.usuarioId,
                userName = usuario.userName,
                password = usuario.password,
                confirmarPassword = usuario.password,
                usuarioSeleccionado = usuario,
                errorMessage = null
            )
        }
    }
}