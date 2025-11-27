package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.PedidosDto

interface PedidosApi {

    @GET("api/Pedidos")
    suspend fun getPedidos(): List<PedidosDto>

    @GET("api/Pedidos/{id}")
    suspend fun getPedido(@Path("id") id: Int): PedidosDto

    @POST("api/Pedidos")
    suspend fun createPedido(@Body data: PedidosDto): PedidosDto

    @PUT("api/Pedidos/{id}")
    suspend fun updatePedido(@Path("id") id: Int, @Body data: PedidosDto): PedidosDto

    @DELETE("api/Pedidos/{id}")
    suspend fun deletePedido(@Path("id") id: Int)
}