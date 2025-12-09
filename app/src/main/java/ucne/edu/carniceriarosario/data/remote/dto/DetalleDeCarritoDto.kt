package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DetalleDeCarritoDto(
    val detalleId: Int = 0,
    val productoId: Int = 0,
    val productoNombre: String = "",
    val carritoId: Int = 0,
    val precio: Float = 0f,
    val cantidad: Int = 0
)
