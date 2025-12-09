package ucne.edu.carniceriarosario.presentation.producto

import ucne.edu.carniceriarosario.data.remote.dto.CategoriaCarnesDto
import ucne.edu.carniceriarosario.data.remote.dto.DetalleProductosDto
import ucne.edu.carniceriarosario.data.remote.dto.ProductosDto

data class ProductoUiState(
    val productos: List<ProductosDto> = emptyList(),
    val isLoadingProductos: Boolean = false,
    val errorProductos: String? = null,

    val productoId: Int = 0,
    val nombre: String = "",
    val imagenProducto: String = "",
    val codigo: String = "",
    val descripcion: String = "",
    val precioLibra: String = "",
    val costoLibra: String = "",
    val stock: String = "",
    val categoriaCarneId: String = "",


    val detalles: List<DetalleProductosDto> = emptyList(),
    val detalleNombre: String = "",
    val detallePrecio: String = "",
    val detalleCantidad: String = "",
    val detalleImagen: String = "",


    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val productoSeleccionado: ProductosDto? = null,


    val searchQuery: String = "",
    val productosFiltrados: List<ProductosDto> = emptyList(),


    val categorias: List<CategoriaCarnesDto> = emptyList(),
    val isLoadingCategorias: Boolean = false
)