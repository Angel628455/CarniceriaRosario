package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagosDto(
    val pagoId: Int = 0,
    val clienteId: String = "",
    val cliente: ClienteDto? = null,
    val montoPagado: Float = 0f,
    val fechaPago: String = "",
    val detallesPagos: List<String> = emptyList()
)