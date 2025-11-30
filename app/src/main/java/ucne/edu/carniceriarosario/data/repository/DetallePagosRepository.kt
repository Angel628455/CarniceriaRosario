package ucne.edu.carniceriarosario.data.repository

import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto
import javax.inject.Inject


class DetallePagosRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getDetallesPagos() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getDetallesPagos()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener detalles de pagos"))
        }
    }

    suspend fun getDetallePago(id: Int) = try {
        Resource.Success(remote.getDetallePago(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener detalle de pago")
    }

    suspend fun createDetallePago(data: DetallesPagosDto) = try {
        Resource.Success(remote.createDetallePago(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear detalle de pago")
    }

    suspend fun updateDetallePago(id: Int, data: DetallesPagosDto) = try {
        Resource.Success(remote.updateDetallePago(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar detalle de pago")
    }

    suspend fun deleteDetallePago(id: Int) = try {
        remote.deleteDetallePago(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar detalle de pago")
    }
}