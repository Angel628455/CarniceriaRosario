package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.EstadosDto

interface EstadosApi {

    @GET("api/Estados")
    suspend fun getEstados(): List<EstadosDto>

    @GET("api/Estados/{id}")
    suspend fun getEstado(@Path("id") id: Int): EstadosDto

    @POST("api/Estados")
    suspend fun createEstado(@Body data: EstadosDto): EstadosDto

    @PUT("api/Estados/{id}")
    suspend fun updateEstado(@Path("id") id: Int, @Body data: EstadosDto): EstadosDto

    @DELETE("api/Estados/{id}")
    suspend fun deleteEstado(@Path("id") id: Int)
}