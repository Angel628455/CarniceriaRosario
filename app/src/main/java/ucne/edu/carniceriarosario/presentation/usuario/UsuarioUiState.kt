package ucne.edu.carniceriarosario.presentation.usuario

import ucne.edu.carniceriarosario.data.remote.dto.UsuarioDto


data class UsuarioUiState(
    val usuarios: List<UsuarioDto> = emptyList(),
    val isLoadingUsuarios: Boolean = false,
    val errorUsuarios: String? = null,
    val usuarioId: Int = 0,
    val userName: String = "",
    val password: String = "",
    val confirmarPassword: String = "",
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,
    val passwordVisible: Boolean = false,
    val confirmarPasswordVisible: Boolean = false,
    val usuarioSeleccionado: UsuarioDto? = null,
    val searchQuery: String = "",
    val usuariosFiltrados: List<UsuarioDto> = emptyList()
)