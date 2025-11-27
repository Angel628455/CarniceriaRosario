package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.EstadisticasDto
import javax.inject.Inject

class EstadisticasRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    fun getEstadisticas(): Flow<Resource<EstadisticasDto>> = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getEstadisticas()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener estadísticas"))
        }
    }
}