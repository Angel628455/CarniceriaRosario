package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.EstadosDto
import javax.inject.Inject

class EstadosRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getEstados() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getEstados()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener estados"))
        }
    }

    suspend fun getEstado(id: Int) = try {
        Resource.Success(remote.getEstado(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener estado")
    }

    suspend fun createEstado(data: EstadosDto) = try {
        Resource.Success(remote.createEstado(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear estado")
    }

    suspend fun updateEstado(id: Int, data: EstadosDto) = try {
        Resource.Success(remote.updateEstado(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar estado")
    }

    suspend fun deleteEstado(id: Int) = try {
        remote.deleteEstado(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar estado")
    }
}