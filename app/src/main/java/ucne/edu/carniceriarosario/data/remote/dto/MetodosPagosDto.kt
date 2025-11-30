package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetodosPagosDto(
    val metodoPagoId: Int = 0,
    val descripcion: String = ""
)