package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsuarioDto(
    val usuarioId: Int = 0,
    val userName: String = "",
    val password: String = ""
)