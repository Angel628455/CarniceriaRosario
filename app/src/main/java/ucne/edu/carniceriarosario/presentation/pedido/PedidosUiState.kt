package ucne.edu.carniceriarosario.presentation.pedido

import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import ucne.edu.carniceriarosario.data.remote.dto.PedidosDto

data class PedidosUiState(
    val pedidos: List<PedidosDto> = emptyList(),
    val isLoadingPedidos: Boolean = false,
    val errorPedidos: String? = null,

    val pedidoId: Int = 0,
    val clienteId: String = "",
    val productosOrdenados: List<DetalleProductosDto> = emptyList(),
    val entrega: String = "",
    val recibido: String = "",
    val montoTotal: Float = 0f,
    val estadoId: Int = 0,

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val pedidoSeleccionado: PedidosDto? = null,
    val searchQuery: String = "",
    val pedidosFiltrados: List<PedidosDto> = emptyList()
)