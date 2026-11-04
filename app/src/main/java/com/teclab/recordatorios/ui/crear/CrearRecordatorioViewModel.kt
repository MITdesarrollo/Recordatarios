package com.teclab.recordatorios.ui.crear

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teclab.recordatorios.data.RecordatoriosRepository
import com.teclab.recordatorios.model.Categoria
import com.teclab.recordatorios.model.NuevoRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.ValidacionRecordatorio
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CrearRecordatorioViewModel(
    private val repositorio: RecordatoriosRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(CrearRecordatorioUiState().validado())
    val uiState: StateFlow<CrearRecordatorioUiState> = _uiState.asStateFlow()

    fun onTituloChange(titulo: String) {
        _uiState.update { it.copy(titulo = titulo, tituloModificado = true).validado() }
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update { it.copy(descripcion = descripcion).validado() }
    }

    fun onCategoriaChange(categoria: Categoria) {
        _uiState.update { it.copy(categoria = categoria).validado() }
    }

    fun onPrioridadChange(prioridad: Prioridad) {
        _uiState.update { it.copy(prioridad = prioridad).validado() }
    }

    fun onGuardar() {
        val estado = _uiState.value
        if (!estado.puedeGuardar) return

        val nuevo = NuevoRecordatorio(
            titulo = estado.titulo.trim(),
            descripcion = estado.descripcion.trim().ifBlank { null },
            categoria = checkNotNull(estado.categoria),
            prioridad = checkNotNull(estado.prioridad),
        )

        _uiState.update { it.copy(envio = EstadoEnvio.Enviando) }
        viewModelScope.launch {
            try {
                repositorio.guardar(nuevo)
                _uiState.update { it.copy(envio = EstadoEnvio.Exito) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(envio = EstadoEnvio.Error(e.message ?: "No se pudo guardar el recordatorio"))
                }
            }
        }
    }

    private fun CrearRecordatorioUiState.validado(): CrearRecordatorioUiState {
        val errorTitulo = ValidacionRecordatorio.validarTitulo(titulo)
        val errorDescripcion = ValidacionRecordatorio.validarDescripcion(descripcion)
        val pendientes = buildList {
            if (errorTitulo != null) add("título")
            if (errorDescripcion != null) add("descripción")
            if (ValidacionRecordatorio.validarCategoria(categoria) != null) add("categoría")
            if (ValidacionRecordatorio.validarPrioridad(prioridad) != null) add("prioridad")
        }
        return copy(
            errorTitulo = errorTitulo.takeIf { tituloModificado },
            errorDescripcion = errorDescripcion,
            camposPendientes = pendientes,
        )
    }
}
