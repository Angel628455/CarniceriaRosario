package ucne.edu.carniceriarosario.presentation.pago

import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto

data class PagosUiState(

    val pagos: List<PagosDto> = emptyList(),
    val isLoadingPagos: Boolean = false,
    val errorPagos: String? = null,


    val detallesPagos: List<DetallesPagosDto> = emptyList(),
    val isLoadingDetallesPagos: Boolean = false,
    val errorDetallesPagos: String? = null,


    val pagoId: Int = 0,
    val clienteId: String = "",
    val montoPagado: Float = 0f,
    val fechaPago: String = "",


    val detallesPagoTemporal: List<DetallePagoTemporal> = emptyList(),


    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,


    val pagoSeleccionado: PagosDto? = null,
    val searchQuery: String = "",
    val pagosFiltrados: List<PagosDto> = emptyList()
)