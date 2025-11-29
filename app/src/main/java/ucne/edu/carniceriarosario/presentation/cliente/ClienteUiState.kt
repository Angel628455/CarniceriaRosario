package ucne.edu.carniceriarosario.presentation.cliente

import ucne.edu.carniceriarosario.data.remote.dto.ClienteDto


data class ClienteUiState(
    val clientes: List<ClienteDto> = emptyList(),
    val isLoadingClientes: Boolean = false,
    val errorClientes: String? = null,

    val clienteId: Int = 0,
    val nombre: String = "",
    val apellidos: String = "",
    val telefono: String = "",

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val clienteSeleccionado: ClienteDto? = null,
    val searchQuery: String = "",
    val clientesFiltrados: List<ClienteDto> = emptyList()
)