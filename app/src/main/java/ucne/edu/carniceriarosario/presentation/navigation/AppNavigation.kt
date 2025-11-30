package ucne.edu.carniceriarosario.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ucne.edu.carniceriarosario.presentation.usuario.CrearUsuarioScreen
import ucne.edu.carniceriarosario.presentation.usuario.EditarUsuarioScreen
import ucne.edu.carniceriarosario.presentation.usuario.LoginScreen
import ucne.edu.carniceriarosario.presentation.usuario.MenuScreen
import ucne.edu.carniceriarosario.presentation.usuario.PerfilScreen
import ucne.edu.carniceriarosario.presentation.usuario.RegisterScreen
import ucne.edu.carniceriarosario.presentation.usuario.StartScreen
import ucne.edu.carniceriarosario.presentation.usuario.UsuarioViewModel
import ucne.edu.carniceriarosario.presentation.usuario.UsuariosScreen


@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.StartScreen
    ) {

        // Pantalla de Inicio (Splash)
        composable<Screen.StartScreen> {
            StartScreen(
                onSplashComplete = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.StartScreen) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Login
        composable<Screen.LoginScreen> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            LoginScreen(
                viewModel = usuarioViewModel,
                onLoginClick = { usuarioId ->
                    navController.navigate(Screen.Menu(usuarioId)) {
                        popUpTo(Screen.LoginScreen) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.RegisterScreen)
                },
                goBack = { navController.navigateUp() }
            )
        }

        // Pantalla de Registro
        composable<Screen.RegisterScreen> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            RegisterScreen(
                viewModel = usuarioViewModel,
                onRegisterClick = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.RegisterScreen) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.RegisterScreen) { inclusive = true }
                    }
                },
                goBack = { navController.navigateUp() }
            )
        }

        // Pantalla de Menú Principal
        composable<Screen.Menu> { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull()
            MenuScreen(
                usuarioId = usuarioId,
                onMenuItemClick = { route ->
                    when (route) {
                        "usuarios" -> navController.navigate(Screen.UsuariosScreen)
                        "carritos" -> navController.navigate(Screen.CarritoListScreen)
                        "categorias" -> navController.navigate(Screen.CategoriaCarnesListScreen)
                        "clientes" -> navController.navigate(Screen.ClienteListScreen)
                        "estados" -> navController.navigate(Screen.EstadosListScreen)
                        "metodosPago" -> navController.navigate(Screen.MetodosPagosListScreen)
                        "pagos" -> navController.navigate(Screen.PagosListScreen)
                        "pedidos" -> navController.navigate(Screen.PedidosListScreen)
                        "productos" -> navController.navigate(Screen.ProductoListScreen)
                    }
                },
                onNavigateToPerfil = { userId ->
                    navController.navigate(Screen.Perfil(userId))
                },
                onLogoutClick = {
                    navController.navigate(Screen.LoginScreen) {
                        popUpTo(Screen.StartScreen) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Perfil
        composable<Screen.Perfil> { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull() ?: 0
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            PerfilScreen(
                usuarioId = usuarioId,
                viewModel = usuarioViewModel,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Pantalla de Lista de Usuarios
        composable<Screen.UsuariosScreen> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            UsuariosScreen(
                viewModel = usuarioViewModel,
                onNavigateToCrearUsuario = {
                    navController.navigate(Screen.CrearUsuario)
                },
                onNavigateToEditarUsuario = { usuarioId ->
                    navController.navigate(Screen.EditarUsuario(usuarioId))
                },
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // Pantalla para Crear Usuario
        composable<Screen.CrearUsuario> {
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            CrearUsuarioScreen(
                viewModel = usuarioViewModel,
                onUsuarioCreado = {
                    navController.navigate(Screen.UsuariosScreen) {
                        popUpTo(Screen.UsuariosScreen) { inclusive = true }
                    }
                },
                onCancelar = { navController.navigateUp() }
            )
        }

        // Pantalla para Editar Usuario
        composable<Screen.EditarUsuario> { backStackEntry ->
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull() ?: 0
            val usuarioViewModel: UsuarioViewModel = hiltViewModel()
            EditarUsuarioScreen(
                usuarioId = usuarioId,
                viewModel = usuarioViewModel,
                onUsuarioActualizado = {
                    navController.navigate(Screen.UsuariosScreen) {
                        popUpTo(Screen.UsuariosScreen) { inclusive = true }
                    }
                },
                onCancelar = { navController.navigateUp() },
                onEliminarUsuario = {
                    navController.navigate(Screen.UsuariosScreen) {
                        popUpTo(Screen.UsuariosScreen) { inclusive = true }
                    }
                }
            )
        }

        // CARROS DE COMPRAS
        composable<Screen.CarritoListScreen> {
            val viewModel: CarritoDeComprasViewModel = hiltViewModel()
            CarritoListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateCarritoScreen(null))
                },
                onNavigateToEdit = { carritoId ->
                    navController.navigate(Screen.CreateCarritoScreen(carritoId))
                }
            )
        }

        composable<Screen.CreateCarritoScreen> { backStackEntry ->
            val carritoId = backStackEntry.arguments?.getString("carritoId")?.toIntOrNull()
            val viewModel: CarritoDeComprasViewModel = hiltViewModel()
            CreateCarritoScreen(
                viewModel = viewModel,
                carritoId = carritoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // CATEGORÍAS
        composable<Screen.CategoriaCarnesListScreen> {
            val viewModel: CategoriaCarnesViewModel = hiltViewModel()
            CategoriaCarnesListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateCategoriaScreen(null))
                },
                onNavigateToEdit = { categoriaId ->
                    navController.navigate(Screen.CreateCategoriaScreen(categoriaId))
                }
            )
        }

        composable<Screen.CreateCategoriaScreen> { backStackEntry ->
            val categoriaId = backStackEntry.arguments?.getString("categoriaId")?.toIntOrNull()
            val viewModel: CategoriaCarnesViewModel = hiltViewModel()
            CreateCategoriaScreen(
                viewModel = viewModel,
                categoriaId = categoriaId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // CLIENTES
        composable<Screen.ClienteListScreen> {
            val viewModel: ClienteViewModel = hiltViewModel()
            ClienteListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateClienteScreen(null))
                },
                onNavigateToEdit = { clienteId ->
                    navController.navigate(Screen.CreateClienteScreen(clienteId))
                }
            )
        }

        composable<Screen.CreateClienteScreen> { backStackEntry ->
            val clienteId = backStackEntry.arguments?.getString("clienteId")?.toIntOrNull()
            val viewModel: ClienteViewModel = hiltViewModel()
            CreateClienteScreen(
                viewModel = viewModel,
                clienteId = clienteId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // ESTADOS
        composable<Screen.EstadosListScreen> {
            val viewModel: EstadosViewModel = hiltViewModel()
            EstadosListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateEstadoScreen(null))
                },
                onNavigateToEdit = { estadoId ->
                    navController.navigate(Screen.CreateEstadoScreen(estadoId))
                }
            )
        }

        composable<Screen.CreateEstadoScreen> { backStackEntry ->
            val estadoId = backStackEntry.arguments?.getString("estadoId")?.toIntOrNull()
            val viewModel: EstadosViewModel = hiltViewModel()
            CreateEstadoScreen(
                viewModel = viewModel,
                estadoId = estadoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // MÉTODOS DE PAGO
        composable<Screen.MetodosPagosListScreen> {
            val viewModel: MetodosPagosViewModel = hiltViewModel()
            MetodosPagosListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateMetodoPagoScreen(null))
                },
                onNavigateToEdit = { metodoPagoId ->
                    navController.navigate(Screen.CreateMetodoPagoScreen(metodoPagoId))
                }
            )
        }

        composable<Screen.CreateMetodoPagoScreen> { backStackEntry ->
            val metodoPagoId = backStackEntry.arguments?.getString("metodoPagoId")?.toIntOrNull()
            val viewModel: MetodosPagosViewModel = hiltViewModel()
            CreateMetodoPagoScreen(
                viewModel = viewModel,
                metodoPagoId = metodoPagoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // PAGOS
        composable<Screen.PagosListScreen> {
            val viewModel: PagosViewModel = hiltViewModel()
            PagosListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreatePagoScreen(null))
                },
                onNavigateToEdit = { pagoId ->
                    navController.navigate(Screen.CreatePagoScreen(pagoId))
                }
            )
        }

        composable<Screen.CreatePagoScreen> { backStackEntry ->
            val pagoId = backStackEntry.arguments?.getString("pagoId")?.toIntOrNull()
            val viewModel: PagosViewModel = hiltViewModel()
            CreatePagoScreen(
                viewModel = viewModel,
                pagoId = pagoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // PEDIDOS
        composable<Screen.PedidosListScreen> {
            val viewModel: PedidosViewModel = hiltViewModel()
            PedidosListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreatePedidoScreen(null))
                },
                onNavigateToEdit = { pedidoId ->
                    navController.navigate(Screen.CreatePedidoScreen(pedidoId))
                }
            )
        }

        composable<Screen.CreatePedidoScreen> { backStackEntry ->
            val pedidoId = backStackEntry.arguments?.getString("pedidoId")?.toIntOrNull()
            val viewModel: PedidosViewModel = hiltViewModel()
            CreatePedidoScreen(
                viewModel = viewModel,
                pedidoId = pedidoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }

        // PRODUCTOS
        composable<Screen.ProductoListScreen> {
            val viewModel: ProductoViewModel = hiltViewModel()
            ProductoListScreen(
                viewModel = viewModel,
                onNavigateToCreate = {
                    navController.navigate(Screen.CreateProductoScreen(null))
                },
                onNavigateToEdit = { productoId ->
                    navController.navigate(Screen.CreateProductoScreen(productoId))
                }
            )
        }

        composable<Screen.CreateProductoScreen> { backStackEntry ->
            val productoId = backStackEntry.arguments?.getString("productoId")?.toIntOrNull()
            val viewModel: ProductoViewModel = hiltViewModel()
            CreateProductoScreen(
                viewModel = viewModel,
                productoId = productoId,
                onNavigateBack = { navController.navigateUp() }
            )
        }
    }
}