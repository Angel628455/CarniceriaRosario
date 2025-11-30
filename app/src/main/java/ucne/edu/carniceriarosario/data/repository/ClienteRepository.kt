package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.ClienteDto
import javax.inject.Inject

class ClienteRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getClientes() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getClientes()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener clientes"))
        }
    }

    suspend fun getCliente(id: Int) = try {
        Resource.Success(remote.getCliente(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener cliente")
    }

    suspend fun createCliente(data: ClienteDto) = try {
        Resource.Success(remote.createCliente(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear cliente")
    }

    suspend fun updateCliente(id: Int, data: ClienteDto) = try {
        Resource.Success(remote.updateCliente(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar cliente")
    }

    suspend fun deleteCliente(id: Int) = try {
        remote.deleteCliente(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar cliente")
    }
}