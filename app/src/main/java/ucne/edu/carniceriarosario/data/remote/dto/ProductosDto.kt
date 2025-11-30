package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductosDto(
    val productoId: Int = 0,
    val nombre: String? = "",
    val imagenProducto: String? = "",
    val codigo: String? = "",
    val descripcion: String? = "",
    val precioLibra: Float = 0f,
    val costoLibra: Float = 0f,
    val stock: Int = 0,
    val fechaIngreso: String = "",
    val categoriaCarneId: Int = 0,
    val categoriaCarnes: CategoriaCarnesDto? = null
)