package ucne.edu.carniceriarosario.presentation.metododepago

import ucne.edu.carniceriarosario.data.remote.dto.MetodosPagosDto


data class MetodosPagosUiState(
    val metodosPagos: List<MetodosPagosDto> = emptyList(),
    val isLoadingMetodosPagos: Boolean = false,
    val errorMetodosPagos: String? = null,

    val metodoPagoId: Int = 0,
    val descripcion: String = "",

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val metodoPagoSeleccionado: MetodosPagosDto? = null,
    val searchQuery: String = "",
    val metodosPagosFiltrados: List<MetodosPagosDto> = emptyList()
)