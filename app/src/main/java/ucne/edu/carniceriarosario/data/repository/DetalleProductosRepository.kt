package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import javax.inject.Inject

class DetalleProductosRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getDetallesProductos() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getDetallesProductos()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener detalles de productos"))
        }
    }

    suspend fun getDetalleProducto(id: Int) = try {
        Resource.Success(remote.getDetalleProducto(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle de producto")
    }

    suspend fun createDetalleProducto(data: DetalleProductosDto) = try {
        Resource.Success(remote.createDetalleProducto(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle de producto")
    }

    suspend fun updateDetalleProducto(id: Int, data: DetalleProductosDto) = try {
        Resource.Success(remote.updateDetalleProducto(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle de producto")
    }

    suspend fun deleteDetalleProducto(id: Int) = try {
        remote.deleteDetalleProducto(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle de producto")
    }
}