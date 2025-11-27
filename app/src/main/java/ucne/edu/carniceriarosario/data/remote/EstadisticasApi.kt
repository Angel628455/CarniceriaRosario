package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.GET
import ucne.edu.carniceriarosario.data.remote.dto.EstadisticasDto

interface EstadisticasApi {

    @GET("estadisticas")
    suspend fun getEstadisticas(): EstadisticasDto
}