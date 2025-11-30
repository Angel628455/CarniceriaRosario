package ucne.edu.carniceriarosario.presentation.categoria

import ucne.edu.carniceriarosario.data.remote.dto.CategoriaCarnesDto

data class CategoriaCarnesUiState(
    val categorias: List<CategoriaCarnesDto> = emptyList(),
    val isLoadingCategorias: Boolean = false,
    val errorCategorias: String? = null,

    val categoriaId: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",

    val isCreating: Boolean = false,
    val isUpdating: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null,

    val categoriaSeleccionada: CategoriaCarnesDto? = null,
    val searchQuery: String = "",
    val categoriasFiltradas: List<CategoriaCarnesDto> = emptyList()
)