package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class DetalleProductosDto(
    val detalleId: Int = 0,
    val productoId: Int? = 0,
    val productos: String? = "",
    val carritoId: Int = 0,
    val precio: Float = 0f,
    val cantidad: Int = 0,
    val imagen: String? = ""
)