package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.UsuarioDto
import javax.inject.Inject

class UsuarioRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource
){

    fun getUsuarios(): Flow<Resource<List<UsuarioDto>>> = flow {
        emit(Resource.Loading())
        try {
            val result = remoteDataSource.getUsuarios()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error desconocido"))
        }
    }

    suspend fun getUsuario(id: Int): Resource<UsuarioDto> = try {
        Resource.Success(remoteDataSource.getUsuario(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener usuario")
    }

    suspend fun createUsuario(usuario: UsuarioDto): Resource<UsuarioDto> = try {
        Resource.Success(remoteDataSource.createUsuario(usuario))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear usuario")
    }

    suspend fun updateUsuario(id: Int, usuario: UsuarioDto): Resource<UsuarioDto> = try {
        Resource.Success(remoteDataSource.updateUsuario(id, usuario))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar usuario")
    }

    suspend fun deleteUsuario(id: Int): Resource<Unit> = try {
        remoteDataSource.deleteUsuario(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar usuario")
    }
}