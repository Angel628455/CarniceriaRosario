package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.CarritoDeComprasDto

interface CarritoApi {

    @GET("api/Carrito")
    suspend fun getCarritos(): List<CarritoDeComprasDto>

    @GET("api/Carrito/{id}")
    suspend fun getCarrito(@Path("id") id: Int): CarritoDeComprasDto

    @POST("api/Carrito")
    suspend fun createCarrito(@Body data: CarritoDeComprasDto): CarritoDeComprasDto

    @PUT("api/Carrito/{id}")
    suspend fun updateCarrito(@Path("id") id: Int, @Body data: CarritoDeComprasDto): CarritoDeComprasDto

    @DELETE("api/Carrito/{id}")
    suspend fun deleteCarrito(@Path("id") id: Int)
}