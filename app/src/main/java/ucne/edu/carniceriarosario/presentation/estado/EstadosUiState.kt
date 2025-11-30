package ucne.edu.carniceriarosario.presentation.estado

import ucne.edu.carniceriarosario.data.remote.dto.EstadosDto

data class EstadosUiState(
    val estados: List<EstadosDto> = emptyList(),
    val isLoadingEstados: Boolean = false,
    val errorEstados: String? = null,

    val estadoId: Int = 0,
    val nombre: String = "",

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val estadoSeleccionado: EstadosDto? = null,
    val searchQuery: String = "",
    val estadosFiltrados: List<EstadosDto> = emptyList()
)