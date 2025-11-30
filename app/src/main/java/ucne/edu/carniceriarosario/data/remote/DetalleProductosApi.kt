package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto

interface DetalleProductosApi {

    @GET("api/DetalleProductos")
    suspend fun getDetalles(): List<DetalleProductosDto>

    @GET("api/DetalleProductos/{id}")
    suspend fun getDetalle(@Path("id") id: Int): DetalleProductosDto

    @POST("api/DetalleProductos")
    suspend fun createDetalle(@Body data: DetalleProductosDto): DetalleProductosDto

    @PUT("api/DetalleProductos/{id}")
    suspend fun updateDetalle(@Path("id") id: Int, @Body data: DetalleProductosDto): DetalleProductosDto

    @DELETE("api/DetalleProductos/{id}")
    suspend fun deleteDetalle(@Path("id") id: Int)
}