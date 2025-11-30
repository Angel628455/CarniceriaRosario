package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DetallesPagosDto(
    val detallePagoId: Int = 0,
    val pedidoId: Int = 0,
    val pedidos: PedidosDto? = null,
    val pagoId: Int = 0,
    val pagos: PagosDto? = null,
    val metodoPagoId: Int = 0,
    val metodosPagos: MetodosPagosDto? = null,
    val montoPagado: Float = 0f
)