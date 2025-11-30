package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ClienteDto(
    val clienteId: Int = 0,
    val nombre: String = "",
    val apellidos: String = "",
    val telefono: String = ""
)