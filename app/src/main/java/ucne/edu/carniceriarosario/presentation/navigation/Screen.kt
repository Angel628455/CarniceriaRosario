package ucne.edu.carniceriarosario.presentation.navigation


import kotlinx.serialization.Serializable

@Serializable
sealed class Screen {

    @Serializable
    data object StartScreen : Screen()

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object RegisterScreen : Screen()

    @Serializable
    data class Menu(val usuarioId: Int? = null) : Screen()

    @Serializable
    data class Perfil(val usuarioId: Int) : Screen()

    // Usuarios
    @Serializable
    data object UsuariosScreen : Screen()

    @Serializable
    data class EditarUsuario(val usuarioId: Int) : Screen()

    @Serializable
    data object CrearUsuario : Screen()

    // Carrito
    @Serializable
    data object CarritoListScreen : Screen()

    @Serializable
    data class CreateCarritoScreen(val carritoId: Int? = null) : Screen()

    // Categorías
    @Serializable
    data object CategoriaCarnesListScreen : Screen()

    @Serializable
    data class CreateCategoriaScreen(val categoriaId: Int? = null) : Screen()

    // Clientes
    @Serializable
    data object ClienteListScreen : Screen()

    @Serializable
    data class CreateClienteScreen(val clienteId: Int? = null) : Screen()

    // Estados
    @Serializable
    data object EstadosListScreen : Screen()

    @Serializable
    data class CreateEstadoScreen(val estadoId: Int? = null) : Screen()

    // Métodos de Pago
    @Serializable
    data object MetodosPagosListScreen : Screen()

    @Serializable
    data class CreateMetodoPagoScreen(val metodoPagoId: Int? = null) : Screen()

    // Pagos
    @Serializable
    data object PagosListScreen : Screen()

    @Serializable
    data class CreatePagoScreen(val pagoId: Int? = null) : Screen()

    // Pedidos
    @Serializable
    data object PedidosListScreen : Screen()

    @Serializable
    data class CreatePedidoScreen(val pedidoId: Int? = null) : Screen()

    // Productos
    @Serializable
    data object ProductoListScreen : Screen()

    @Serializable
    data class CreateProductoScreen(val productoId: Int? = null) : Screen()
}