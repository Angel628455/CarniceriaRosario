package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PedidosDto(
    val pedidoId: Int = 0,
    val clienteId: String? = "",
    val productosOrdenados: List<DetalleProductosDto> = emptyList(),
    val entrega: String = "",
    val recibido: String = "",
    val montoTotal: Float = 0f,
    val estadoId: Int = 0,
    val estados: EstadosDto? = null
)