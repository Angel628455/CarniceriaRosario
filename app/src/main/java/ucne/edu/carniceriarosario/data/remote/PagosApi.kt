package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto

interface PagosApi {

    @GET("api/Pagos")
    suspend fun getPagos(): List<PagosDto>

    @GET("api/Pagos/{id}")
    suspend fun getPago(@Path("id") id: Int): PagosDto

    @POST("api/Pagos")
    suspend fun createPago(@Body data: PagosDto): PagosDto

    @PUT("api/Pagos/{id}")
    suspend fun updatePago(@Path("id") id: Int, @Body data: PagosDto): PagosDto

    @DELETE("api/Pagos/{id}")
    suspend fun deletePago(@Path("id") id: Int)
}