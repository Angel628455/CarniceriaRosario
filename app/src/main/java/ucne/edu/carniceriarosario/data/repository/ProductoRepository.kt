package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.ProductosDto
import javax.inject.Inject

class ProductoRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){

    fun getProductos(): Flow<Resource<List<ProductosDto>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remoteDataSource.getProductos()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener productos"))
        }
    }

    suspend fun getProducto(id: Int): Resource<ProductosDto> = try {
        Resource.Success(remoteDataSource.getProducto(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener producto")
    }

    suspend fun createProducto(dto: ProductosDto): Resource<ProductosDto> = try {
        Resource.Success(remoteDataSource.createProducto(dto))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear producto")
    }

    suspend fun updateProducto(id: Int, dto: ProductosDto): Resource<ProductosDto> = try {
        Resource.Success(remoteDataSource.updateProducto(id, dto))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar producto")
    }

    suspend fun deleteProducto(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteProducto(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar producto")
    }
}