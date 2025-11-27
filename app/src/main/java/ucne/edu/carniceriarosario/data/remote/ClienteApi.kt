package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.ClienteDto

interface ClienteApi {

    @GET("api/Clientes")
    suspend fun getClientes(): List<ClienteDto>

    @GET("api/Clientes/{id}")
    suspend fun getCliente(@Path("id") id: Int): ClienteDto

    @POST("api/Clientes")
    suspend fun createCliente(@Body data: ClienteDto): ClienteDto

    @PUT("api/Clientes/{id}")
    suspend fun updateCliente(@Path("id") id: Int, @Body data: ClienteDto): ClienteDto

    @DELETE("api/Clientes/{id}")
    suspend fun deleteCliente(@Path("id") id: Int)
}