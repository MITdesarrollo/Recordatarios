package com.teclab.recordatorios.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teclab.recordatorios.data.RecordatoriosRepository
import com.teclab.recordatorios.ui.navigation.Destino
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetalleRecordatorioViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositorio: RecordatoriosRepository,
) : ViewModel() {

    private val id: Int = checkNotNull(savedStateHandle[Destino.Detalle.ARG_ID])

    private val _uiState = MutableStateFlow<DetalleUiState>(DetalleUiState.Cargando)
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun onReintentar() = cargar()

    private fun cargar() {
        viewModelScope.launch {
            _uiState.update { DetalleUiState.Cargando }
            try {
                val recordatorio = repositorio.obtenerPorId(id)
                _uiState.update {
                    if (recordatorio == null) DetalleUiState.NoEncontrado
                    else DetalleUiState.Encontrado(recordatorio)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update {
                    DetalleUiState.Error(e.message ?: "No se pudo cargar el recordatorio")
                }
            }
        }
    }
}
