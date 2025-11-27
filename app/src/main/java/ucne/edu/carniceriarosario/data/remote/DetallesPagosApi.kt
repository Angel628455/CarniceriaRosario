package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto

interface DetallesPagosApi {

    @GET("api/DetallesPagos")
    suspend fun getDetallesPagos(): List<DetallesPagosDto>

    @GET("api/DetallesPagos/{id}")
    suspend fun getDetallePago(@Path("id") id: Int): DetallesPagosDto

    @POST("api/DetallesPagos")
    suspend fun createDetallePago(@Body data: DetallesPagosDto): DetallesPagosDto

    @PUT("api/DetallesPagos/{id}")
    suspend fun updateDetallePago(@Path("id") id: Int, @Body data: DetallesPagosDto): DetallesPagosDto

    @DELETE("api/DetallesPagos/{id}")
    suspend fun deleteDetallePago(@Path("id") id: Int)
}