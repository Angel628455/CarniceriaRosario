package ucne.edu.carniceriarosario.presentation.pago

import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto

data class PagosUiState(
    // Estados para Pagos
    val pagos: List<PagosDto> = emptyList(),
    val isLoadingPagos: Boolean = false,
    val errorPagos: String? = null,

    // Estados para Detalles de Pagos
    val detallesPagos: List<DetallesPagosDto> = emptyList(),
    val isLoadingDetallesPagos: Boolean = false,
    val errorDetallesPagos: String? = null,

    // Formulario de Pago
    val pagoId: Int = 0,
    val clienteId: String = "",
    val montoPagado: Float = 0f,
    val fechaPago: String = "",

    // Detalles temporales para el formulario
    val detallesPagoTemporal: List<DetallePagoTemporal> = emptyList(),

    // Estados de operaciones
    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    // Búsqueda y selección
    val pagoSeleccionado: PagosDto? = null,
    val searchQuery: String = "",
    val pagosFiltrados: List<PagosDto> = emptyList()
)