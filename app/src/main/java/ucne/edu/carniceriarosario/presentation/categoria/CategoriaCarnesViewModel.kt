package ucne.edu.carniceriarosario.presentation.categoria

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

import ucne.edu.carniceriarosario.data.remote.Resource
import ucne.edu.carniceriarosario.data.remote.dto.CategoriaCarnesDto
import ucne.edu.carniceriarosario.data.repository.CategoriaRepository
import ucne.edu.carniceriarosario.presentation.categoria.CategoriaCarnesUiState


@HiltViewModel
class CategoriaCarnesViewModel @Inject constructor(
    private val repository: CategoriaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoriaCarnesUiState())
    val uiState: StateFlow<CategoriaCarnesUiState> = _uiState.asStateFlow()

    init {
        loadCategorias()
    }

    fun loadCategorias() {
        viewModelScope.launch {
            repository.getCategorias().collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                categorias = resource.data,
                                categoriasFiltradas = resource.data,
                                isLoadingCategorias = false,
                                errorCategorias = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoadingCategorias = false,
                                errorCategorias = resource.message,
                                categorias = emptyList(),
                                categoriasFiltradas = emptyList()
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoadingCategorias = true, errorCategorias = null)
                        }
                    }
                }
            }
        }
    }

    fun createCategoria() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isCreating = true, errorMessage = null) }

        viewModelScope.launch {
            val nuevaCategoria = CategoriaCarnesDto(
                categoriaCarnesId = 0,
                nombre = currentState.nombre,
                descripcion = currentState.descripcion
            )

            when (val result = repository.createCategoria(nuevaCategoria)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            successMessage = "Categoría creada exitosamente",
                            errorMessage = null
                        )
                    }
                    clearForm()
                    loadCategorias()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isCreating = false,
                            errorMessage = result.message,
                            successMessage = null
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun updateCategoria() {
        val currentState = _uiState.value

        if (currentState.nombre.isBlank()) {
            _uiState.update { it.copy(errorMessage = "El nombre es requerido") }
            return
        }

        _uiState.update { it.copy(isUpdating = true, errorMessage = null) }

        viewModelScope.launch {
            val categoriaActualizada = CategoriaCarnesDto(
                categoriaCarnesId = currentState.categoriaId,
                nombre = currentState.nombre,
                descripcion = currentState.descripcion
            )

            when (val result = repository.updateCategoria(currentState.categoriaId, categoriaActualizada)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            successMessage = "Categoría actualizada exitosamente",
                            errorMessage = null
                        )
                    }
                    loadCategorias()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isUpdating = false,
                            errorMessage = result.message,
                            successMessage = null
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun deleteCategoria(id: Int) {
        _uiState.update { it.copy(isDeleting = true, errorMessage = null) }

        viewModelScope.launch {
            when (val result = repository.deleteCategoria(id)) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            successMessage = "Categoría eliminada exitosamente",
                            errorMessage = null
                        )
                    }
                    loadCategorias()
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isDeleting = false,
                            errorMessage = result.message,
                            successMessage = null
                        )
                    }
                }
                else -> {}
            }
        }
    }

    fun setNombre(nombre: String) {
        _uiState.update { it.copy(nombre = nombre, errorMessage = null) }
    }

    fun setDescripcion(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion, errorMessage = null) }
    }

    fun setSearchQuery(query: String) {
        val categoriasFiltradas = if (query.isBlank()) {
            _uiState.value.categorias
        } else {
            _uiState.value.categorias.filter { categoria ->
                categoria.nombre.contains(query, ignoreCase = true) ||
                        categoria.descripcion.contains(query, ignoreCase = true)
            }
        }

        _uiState.update {
            it.copy(
                searchQuery = query,
                categoriasFiltradas = categoriasFiltradas
            )
        }
    }

    fun clearMessages() {
        _uiState.update {
            it.copy(
                successMessage = null,
                errorMessage = null,
                errorCategorias = null
            )
        }
    }

    fun clearForm() {
        _uiState.update {
            it.copy(
                categoriaId = 0,
                nombre = "",
                descripcion = "",
                categoriaSeleccionada = null,
                errorMessage = null
            )
        }
    }

    fun setCategoriaForEdit(categoria: CategoriaCarnesDto) {
        _uiState.update {
            it.copy(
                categoriaId = categoria.categoriaCarnesId,
                nombre = categoria.nombre,
                descripcion = categoria.descripcion,
                categoriaSeleccionada = categoria,
                errorMessage = null
            )
        }
    }
}