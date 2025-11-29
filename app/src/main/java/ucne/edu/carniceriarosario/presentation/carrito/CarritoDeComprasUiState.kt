package ucne.edu.carniceriarosario.presentation.carrito

import ucne.edu.carniceriarosario.data.remote.dto.CarritoDeComprasDto
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto

data class CarritoDeComprasUiState(
    val carritos: List<CarritoDeComprasDto> = emptyList(),
    val isLoadingCarritos: Boolean = false,
    val errorCarritos: String? = null,

    val carritoId: Int = 0,
    val clienteId: String = "",
    val productos: List<DetalleProductosDto> = emptyList(),
    val montoTotal: Float = 0f,
    val compra: Boolean = false,

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val carritoSeleccionado: CarritoDeComprasDto? = null,
    val searchQuery: String = "",
    val carritosFiltrados: List<CarritoDeComprasDto> = emptyList()
)