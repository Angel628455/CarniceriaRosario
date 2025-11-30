package ucne.edu.carniceriarosario.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: UsuarioViewModel = hiltViewModel(),
    onLoginClick: (Int) -> Unit,
    onNavigateToRegister: () -> Unit,
    goBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Iniciar Sesión") },
                navigationIcon = {
                    IconButton(onClick = goBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )

            OutlinedTextField(
                value = uiState.userName,
                onValueChange = { viewModel.setUserName(it) },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.setPassword(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.passwordVisible) {
                                Icons.Default.Lock
                            } else {
                                Icons.Default.Lock
                            },
                            contentDescription = if (uiState.passwordVisible) {
                                "Ocultar contraseña"
                            } else {
                                "Mostrar contraseña"
                            }
                        )
                    }
                }
            )

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    // Simular login exitoso
                    if (uiState.userName.isNotBlank() && uiState.password.isNotBlank()) {
                        // En una app real, aquí validarías con el backend
                        // Por ahora usamos un ID temporal
                        onLoginClick(1)
                    } else {
                        // Mostrar error
                        viewModel.uiState.value.copy(
                            errorMessage = "Usuario y contraseña requeridos"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text("Iniciar Sesión")
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("¿No tienes cuenta? Regístrate")
            }
        }
    }
}