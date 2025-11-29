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
            // Obtener el usuarioId de los argumentos
            val usuarioId = backStackEntry.arguments?.getString("usuarioId")?.toIntOrNull()
            MenuScreen(
                usuarioId = usuarioId,
                onMenuItemClick = { route ->
                    when (route) {
                        "usuarios" -> navController.navigate(Screen.UsuariosScreen)
                        // Aquí puedes agregar más rutas según tus necesidades
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
    }
}