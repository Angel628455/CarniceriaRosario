package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.MetodosPagosDto
import javax.inject.Inject

class MetodosPagosRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getMetodosPago() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getMetodosPago()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener métodos de pago"))
        }
    }

    suspend fun getMetodoPago(id: Int) = try {
        Resource.Success(remote.getMetodoPago(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener método de pago")
    }

    suspend fun createMetodoPago(data: MetodosPagosDto) = try {
        Resource.Success(remote.createMetodoPago(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear método de pago")
    }

    suspend fun updateMetodoPago(id: Int, data: MetodosPagosDto) = try {
        Resource.Success(remote.updateMetodoPago(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar método de pago")
    }

    suspend fun deleteMetodoPago(id: Int) = try {
        remote.deleteMetodoPago(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar método de pago")
    }
}