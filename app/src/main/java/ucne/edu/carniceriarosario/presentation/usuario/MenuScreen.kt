package ucne.edu.carniceriarosario.presentation.usuario

//import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

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
                item {
                    MenuCard(
                        title = "Usuarios",
                        icon = Icons.Default.Person,
                        onClick = { onMenuItemClick("usuarios") }
                    )
                }

                item {
                    MenuCard(
                        title = "Productos",
                        icon = Icons.Default.ShoppingCart,
                        onClick = { onMenuItemClick("productos") }
                    )
                }

                item {
                    MenuCard(
                        title = "Categorías",
                        icon = Icons.Default.Send,
                        onClick = { onMenuItemClick("categorias") }
                    )
                }

                item {
                    MenuCard(
                        title = "Clientes",
                        icon = Icons.Default.Face,
                        onClick = { onMenuItemClick("clientes") }
                    )
                }

                item {
                    MenuCard(
                        title = "Carritos",
                        icon = Icons.Default.ShoppingCart,
                        onClick = { onMenuItemClick("carritos") }
                    )
                }

                item {
                    MenuCard(
                        title = "Pedidos",
                        icon = Icons.Default.Home,
                        onClick = { onMenuItemClick("pedidos") }
                    )
                }

                item {
                    MenuCard(
                        title = "Pagos",
                        icon = Icons.Default.ArrowDropDown,
                        onClick = { onMenuItemClick("pagos") }
                    )
                }

                item {
                    MenuCard(
                        title = "Estados",
                        icon = Icons.Default.AccountCircle,
                        onClick = { onMenuItemClick("estados") }
                    )
                }

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
    val startColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
    val endColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(20.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        colors = listOf(startColor, endColor)
                    )
                )
                .padding(18.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    modifier = Modifier.size(52.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}