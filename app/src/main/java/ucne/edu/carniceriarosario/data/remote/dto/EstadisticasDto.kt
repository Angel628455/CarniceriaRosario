package ucne.edu.carniceriarosario.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class EstadisticasDto(
    val estadisticaId: Int = 0,
    val totalPedidos: Int = 0,
    val totalPagos: Int = 0,
    val ingresosTotales: Float = 0f,
    val totalClientes: Int = 0,
    val totalProductos: Int = 0,
    val ventasHoy: Float = 0f,
    val pedidosHoy: Int = 0,
    val fechaActualizacion: String = ""
)