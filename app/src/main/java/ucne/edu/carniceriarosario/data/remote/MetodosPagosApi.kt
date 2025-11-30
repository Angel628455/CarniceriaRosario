package ucne.edu.carniceriarosario.data.remote


import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.MetodosPagosDto

interface MetodosPagosApi {

    @GET("api/MetodosPagos")
    suspend fun getMetodos(): List<MetodosPagosDto>

    @GET("api/MetodosPagos/{id}")
    suspend fun getMetodo(@Path("id") id: Int): MetodosPagosDto

    @POST("api/MetodosPagos")
    suspend fun createMetodo(@Body data: MetodosPagosDto): MetodosPagosDto

    @PUT("api/MetodosPagos/{id}")
    suspend fun updateMetodo(@Path("id") id: Int, @Body data: MetodosPagosDto): MetodosPagosDto

    @DELETE("api/MetodosPagos/{id}")
    suspend fun deleteMetodo(@Path("id") id: Int)
}