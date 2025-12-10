package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DetallesPagosDto(
    val detallePagoId: Int = 0,
    val pedidoId: Int,
    val pagoId: Int,
    val metodoPagoId: Int,
    val montoPagado: Float
)