package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EstadosDto(
    val estadosId: Int = 0,
    val nombre: String? = ""
)