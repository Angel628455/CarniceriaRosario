package ucne.edu.carniceriarosario.data.remote

import ucne.edu.carniceriarosario.data.remote.dto.CarritoDeComprasDto
import ucne.edu.carniceriarosario.data.remote.dto.CategoriaCarnesDto
import ucne.edu.carniceriarosario.data.remote.dto.ClienteDto
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import ucne.edu.carniceriarosario.data.remote.dto.DetallesPagosDto
import ucne.edu.carniceriarosario.data.remote.dto.EstadosDto
import ucne.edu.carniceriarosario.data.remote.dto.MetodosPagosDto
import ucne.edu.carniceriarosario.data.remote.dto.PagosDto
import ucne.edu.carniceriarosario.data.remote.dto.PedidosDto
import ucne.edu.carniceriarosario.data.remote.dto.ProductosDto
import ucne.edu.carniceriarosario.data.remote.dto.UsuarioDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val usuarioApi: UsuarioApi,
    private val carritoApi: CarritoApi,
    private val categoriaApi: CategoriaCarnesApi,
    private val clienteApi: ClienteApi,
    private val detalleProductosApi: DetalleProductosApi,
    private val detallePagosApi: DetallesPagosApi,
    private val estadosApi: EstadosApi,
    private val metodosPagosApi: MetodosPagosApi,
    private val pagosApi: PagosApi,
    private val pedidosApi: PedidosApi,
    private val productosApi: ProductosApi,
    private val estadisticasApi: EstadisticasApi

) {

    // ------------------------- USUARIOS -------------------------
    suspend fun getUsuarios() = usuarioApi.getUsuarios()
    suspend fun getUsuario(id: Int) = usuarioApi.getUsuario(id)
    suspend fun createUsuario(data: UsuarioDto) = usuarioApi.createUsuario(data)
    suspend fun updateUsuario(id: Int, data: UsuarioDto) = usuarioApi.updateUsuario(id, data)
    suspend fun deleteUsuario(id: Int) = usuarioApi.deleteUsuario(id)

    // --------------------- ESTADISTICAS ---------------------
    suspend fun getEstadisticas() = estadisticasApi.getEstadisticas()

    // ------------------------- CARRITOS -------------------------
    suspend fun getCarritos() = carritoApi.getCarritos()
    suspend fun getCarrito(id: Int) = carritoApi.getCarrito(id)
    suspend fun createCarrito(data: CarritoDeComprasDto) = carritoApi.createCarrito(data)
    suspend fun updateCarrito(id: Int, data: CarritoDeComprasDto) = carritoApi.updateCarrito(id, data)
    suspend fun deleteCarrito(id: Int) = carritoApi.deleteCarrito(id)

    // ------------------------- CATEGORIAS -------------------------
    suspend fun getCategorias() = categoriaApi.getCategorias()
    suspend fun getCategoria(id: Int) = categoriaApi.getCategoria(id)
    suspend fun createCategoria(data: CategoriaCarnesDto) = categoriaApi.createCategoria(data)
    suspend fun updateCategoria(id: Int, data: CategoriaCarnesDto) = categoriaApi.updateCategoria(id, data)
    suspend fun deleteCategoria(id: Int) = categoriaApi.deleteCategoria(id)

    // ------------------------- CLIENTES -------------------------
    suspend fun getClientes() = clienteApi.getClientes()
    suspend fun getCliente(id: Int) = clienteApi.getCliente(id)
    suspend fun createCliente(data: ClienteDto) = clienteApi.createCliente(data)
    suspend fun updateCliente(id: Int, data: ClienteDto) = clienteApi.updateCliente(id, data)
    suspend fun deleteCliente(id: Int) = clienteApi.deleteCliente(id)

    // ------------------------- DETALLE PRODUCTOS -------------------------
    suspend fun getDetallesProductos() = detalleProductosApi.getDetalles()
    suspend fun getDetalleProducto(id: Int) = detalleProductosApi.getDetalle(id)
    suspend fun createDetalleProducto(data: DetalleProductosDto) = detalleProductosApi.createDetalle(data)
    suspend fun updateDetalleProducto(id: Int, data: DetalleProductosDto) = detalleProductosApi.updateDetalle(id, data)
    suspend fun deleteDetalleProducto(id: Int) = detalleProductosApi.deleteDetalle(id)

    // ------------------------- DETALLE PAGOS -------------------------
    suspend fun getDetallesPagos() = detallePagosApi.getDetallesPagos()
    suspend fun getDetallePago(id: Int) = detallePagosApi.getDetallePago(id)
    suspend fun createDetallePago(data: DetallesPagosDto) = detallePagosApi.createDetallePago(data)
    suspend fun updateDetallePago(id: Int, data: DetallesPagosDto) = detallePagosApi.updateDetallePago(id, data)
    suspend fun deleteDetallePago(id: Int) = detallePagosApi.deleteDetallePago(id)

    // ------------------------- ESTADOS -------------------------
    suspend fun getEstados() = estadosApi.getEstados()
    suspend fun getEstado(id: Int) = estadosApi.getEstado(id)
    suspend fun createEstado(data: EstadosDto) = estadosApi.createEstado(data)
    suspend fun updateEstado(id: Int, data: EstadosDto) = estadosApi.updateEstado(id, data)
    suspend fun deleteEstado(id: Int) = estadosApi.deleteEstado(id)

    // ------------------------- METODOS DE PAGO -------------------------
    suspend fun getMetodosPago() = metodosPagosApi.getMetodos()
    suspend fun getMetodoPago(id: Int) = metodosPagosApi.getMetodo(id)
    suspend fun createMetodoPago(data: MetodosPagosDto) = metodosPagosApi.createMetodo(data)
    suspend fun updateMetodoPago(id: Int, data: MetodosPagosDto) = metodosPagosApi.updateMetodo(id, data)
    suspend fun deleteMetodoPago(id: Int) = metodosPagosApi.deleteMetodo(id)

    // ------------------------- PAGOS -------------------------
    suspend fun getPagos() = pagosApi.getPagos()
    suspend fun getPago(id: Int) = pagosApi.getPago(id)
    suspend fun createPago(data: PagosDto) = pagosApi.createPago(data)
    suspend fun updatePago(id: Int, data: PagosDto) = pagosApi.updatePago(id, data)
    suspend fun deletePago(id: Int) = pagosApi.deletePago(id)

    // ------------------------- PEDIDOS -------------------------
    suspend fun getPedidos() = pedidosApi.getPedidos()
    suspend fun getPedido(id: Int) = pedidosApi.getPedido(id)
    suspend fun createPedido(data: PedidosDto) = pedidosApi.createPedido(data)
    suspend fun updatePedido(id: Int, data: PedidosDto) = pedidosApi.updatePedido(id, data)
    suspend fun deletePedido(id: Int) = pedidosApi.deletePedido(id)

    // ------------------------- PRODUCTOS -------------------------
    suspend fun getProductos() = productosApi.getProductos()
    suspend fun getProducto(id: Int) = productosApi.getProducto(id)
    suspend fun createProducto(data: ProductosDto) = productosApi.createProducto(data)
    suspend fun updateProducto(id: Int, data: ProductosDto) = productosApi.updateProducto(id, data)
    suspend fun deleteProducto(id: Int) = productosApi.deleteProducto(id)
}