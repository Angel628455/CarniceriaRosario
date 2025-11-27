package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.ProductosDto

interface ProductosApi {

    @GET("api/Productos")
    suspend fun getProductos(): List<ProductosDto>

    @GET("api/Productos/{id}")
    suspend fun getProducto(@Path("id") id: Int): ProductosDto

    @POST("api/Productos")
    suspend fun createProducto(@Body data: ProductosDto): ProductosDto

    @PUT("api/Productos/{id}")
    suspend fun updateProducto(@Path("id") id: Int, @Body data: ProductosDto): ProductosDto

    @DELETE("api/Productos/{id}")
    suspend fun deleteProducto(@Path("id") id: Int)
}