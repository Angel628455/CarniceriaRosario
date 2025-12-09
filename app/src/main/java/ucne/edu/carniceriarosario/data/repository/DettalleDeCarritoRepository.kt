package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import javax.inject.Inject

class DettalleDeCarritoRepository @Inject constructor(
    private val remote: RemoteDataSource
){
    fun getDetallesDeCarrito() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getDetallesDeCarrito()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener detalles de productos"))
        }
    }
}