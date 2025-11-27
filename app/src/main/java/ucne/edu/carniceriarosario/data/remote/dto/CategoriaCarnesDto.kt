package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoriaCarnesDto(
    val categoriaCarnesId: Int = 0,
    val nombre: String = "",
    val descripcion: String = ""
)