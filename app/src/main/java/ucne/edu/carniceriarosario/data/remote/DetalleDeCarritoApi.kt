package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import ucne.edu.carniceriarosario.data.remote.dto.CarritoDeComprasDto
import ucne.edu.carniceriarosario.data.remote.dto.DetalleDeCarritoDto

interface DetalleDeCarritoApi {
//    @GET("api/Carrito/{id}")
//    suspend fun getCarrito(@Path("id") id: Int): CarritoDeComprasDto

    @GET("api/CarritoDeCompras")
    suspend fun getCarritos(): List<DetalleDeCarritoDto>

//    @POST("api/Carrito")
//    suspend fun createCarrito(@Body data: CarritoDeComprasDto): CarritoDeComprasDto
//
//    @PUT("api/Carrito/{id}")
//    suspend fun updateCarrito(@Path("id") id: Int, @Body data: CarritoDeComprasDto): CarritoDeComprasDto
//
//    @DELETE("api/Carrito/{id}")
//    suspend fun deleteCarrito(@Path("id") id: Int)

}