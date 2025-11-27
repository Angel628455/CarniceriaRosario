package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.*
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.CarritoDeComprasDto
import javax.inject.Inject

class CarritoRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    fun getCarritos(): Flow<Resource<List<CarritoDeComprasDto>>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getCarritos()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener carritos"))
        }
    }

    suspend fun getCarrito(id: Int) = try {
        Resource.Success(remote.getCarrito(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener carrito")
    }

    suspend fun createCarrito(data: CarritoDeComprasDto) = try {
        Resource.Success(remote.createCarrito(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear carrito")
    }

    suspend fun updateCarrito(id: Int, data: CarritoDeComprasDto) = try {
        Resource.Success(remote.updateCarrito(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar carrito")
    }

    suspend fun deleteCarrito(id: Int) = try {
        remote.deleteCarrito(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar carrito")
    }
}