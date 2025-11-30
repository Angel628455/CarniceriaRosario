package ucne.edu.carniceriarosario.presentation.usuario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    usuarioId: Int?,
    onMenuItemClick: (String) -> Unit,
    onNavigateToPerfil: (Int) -> Unit,
    onLogoutClick: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Carnicería Rosarios",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { usuarioId?.let { onNavigateToPerfil(it) } }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil"
                        )
                    }
                    IconButton(onClick = { showLogoutDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Cerrar sesión",
                            tint = MaterialTheme.colorScheme.error
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
                .padding(16.dp)
        ) {
            Text(
                text = "Sistema de Gestión",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Gestión de Usuarios
                item {
                    MenuCard(
                        title = "Usuarios",
                        icon = Icons.Default.Person,
                        onClick = { onMenuItemClick("usuarios") }
                    )
                }

                // Gestión de Productos
                item {
                    MenuCard(
                        title = "Productos",
                        icon = Icons.Default.ShoppingCart,
                        onClick = { onMenuItemClick("productos") }
                    )
                }

                // Gestión de Categorías
                item {
                    MenuCard(
                        title = "Categorías",
                        icon = Icons.Default.Send,
                        onClick = { onMenuItemClick("categorias") }
                    )
                }

                // Gestión de Clientes
                item {
                    MenuCard(
                        title = "Clientes",
                        icon = Icons.Default.Face,
                        onClick = { onMenuItemClick("clientes") }
                    )
                }

                // Gestión de Carritos
                item {
                    MenuCard(
                        title = "Carritos",
                        icon = Icons.Default.ShoppingCart,
                        onClick = { onMenuItemClick("carritos") }
                    )
                }

                // Gestión de Pedidos
                item {
                    MenuCard(
                        title = "Pedidos",
                        icon = Icons.Default.Home,
                        onClick = { onMenuItemClick("pedidos") }
                    )
                }

                // Gestión de Pagos
                item {
                    MenuCard(
                        title = "Pagos",
                        icon = Icons.Default.ArrowDropDown,
                        onClick = { onMenuItemClick("pagos") }
                    )
                }

                // Gestión de Estados
                item {
                    MenuCard(
                        title = "Estados",
                        icon = Icons.Default.AccountCircle,
                        onClick = { onMenuItemClick("estados") }
                    )
                }

                // Gestión de Métodos de Pago
                item {
                    MenuCard(
                        title = "Métodos Pago",
                        icon = Icons.Default.Favorite,
                        onClick = { onMenuItemClick("metodosPago") }
                    )
                }
            }
        }
    }

    // Diálogo de confirmación de cierre de sesión
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas cerrar sesión?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        onLogoutClick()
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Cerrar Sesión")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun MenuCard(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
        }
    }
}