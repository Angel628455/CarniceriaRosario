package ucne.edu.carniceriarosario.data.remote

import retrofit2.http.*
import ucne.edu.carniceriarosario.data.remote.dto.CategoriaCarnesDto

interface CategoriaCarnesApi {

    @GET("api/CategoriasCarnes")
    suspend fun getCategorias(): List<CategoriaCarnesDto>

    @GET("api/CategoriasCarnes/{id}")
    suspend fun getCategoria(@Path("id") id: Int): CategoriaCarnesDto

    @POST("api/CategoriasCarnes")
    suspend fun createCategoria(@Body data: CategoriaCarnesDto): CategoriaCarnesDto

    @PUT("api/CategoriasCarnes/{id}")
    suspend fun updateCategoria(@Path("id") id: Int, @Body data: CategoriaCarnesDto): CategoriaCarnesDto

    @DELETE("api/CategoriasCarnes/{id}")
    suspend fun deleteCategoria(@Path("id") id: Int)
}