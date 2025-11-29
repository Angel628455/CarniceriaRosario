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

    @Serializable
    data object UsuariosScreen : Screen()

    @Serializable
    data class EditarUsuario(val usuarioId: Int) : Screen()

    @Serializable
    data object CrearUsuario : Screen()
}