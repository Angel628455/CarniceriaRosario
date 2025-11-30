package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.CategoriaCarnesDto
import javax.inject.Inject

class CategoriaRepository @Inject constructor(
    private val remote: RemoteDataSource
) {

    fun getCategorias() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getCategorias()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener categorías"))
        }
    }

    suspend fun getCategoria(id: Int) = try {
        Resource.Success(remote.getCategoria(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener categoría")
    }

    suspend fun createCategoria(data: CategoriaCarnesDto) = try {
        Resource.Success(remote.createCategoria(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear categoría")
    }

    suspend fun updateCategoria(id: Int, data: CategoriaCarnesDto) = try {
        Resource.Success(remote.updateCategoria(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar categoría")
    }

    suspend fun deleteCategoria(id: Int) = try {
        remote.deleteCategoria(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar categoría")
    }
}