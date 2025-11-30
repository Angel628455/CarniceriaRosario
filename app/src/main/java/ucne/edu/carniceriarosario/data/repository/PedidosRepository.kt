package ucne.edu.carniceriarosario.data.repository
import kotlinx.coroutines.flow.flow
import ucne.edu.carniceriarosario.data.remote.RemoteDataSource
import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.PedidosDto
import javax.inject.Inject

class PedidosRepository @Inject constructor(
    private val remote: RemoteDataSource
){

    fun getPedidos() = flow {
        emit(Resource.Loading())
        try {
            emit(Resource.Success(remote.getPedidos()))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Error al obtener pedidos"))
        }
    }

    suspend fun getPedido(id: Int) = try {
        Resource.Success(remote.getPedido(id))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al obtener pedido")
    }

    suspend fun createPedido(data: PedidosDto) = try {
        Resource.Success(remote.createPedido(data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al crear pedido")
    }

    suspend fun updatePedido(id: Int, data: PedidosDto) = try {
        Resource.Success(remote.updatePedido(id, data))
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al actualizar pedido")
    }

    suspend fun deletePedido(id: Int) = try {
        remote.deletePedido(id)
        Resource.Success(Unit)
    } catch (e: Exception) {
        Resource.Error(e.message ?: "Error al eliminar pedido")
    }
}