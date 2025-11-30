package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CarritoDeComprasDto(
    val carritoId: Int = 0,
    val clienteId: String = "",
    val productos: List<DetalleProductosDto> = emptyList(),
    val montoTotal: Float = 0f,
    val compra: Boolean = false
)