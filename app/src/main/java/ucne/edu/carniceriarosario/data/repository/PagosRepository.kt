package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto
import javax.inject.Inject

class PagosRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getPagos() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getPagos()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener pagos"))
        }
    }

    suspend fun getPago(id: Int) = try {
        Resource.Success(remote.getPago(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener pago")
    }

    suspend fun createPago(data: PagosDto) = try {
        Resource.Success(remote.createPago(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear pago")
    }

    suspend fun updatePago(id: Int, data: PagosDto) = try {
        Resource.Success(remote.updatePago(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar pago")
    }

    suspend fun deletePago(id: Int) = try {
        remote.deletePago(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar pago")
    }
}