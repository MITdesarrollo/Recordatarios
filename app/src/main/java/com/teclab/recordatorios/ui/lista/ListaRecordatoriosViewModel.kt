package com.teclab.recordatorios.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teclab.recordatorios.data.RecordatoriosRepository
import com.teclab.recordatorios.data.Simulacion
import com.teclab.recordatorios.data.SimuladorEscenarios
import com.teclab.recordatorios.model.EstadoRecordatorio
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListaRecordatoriosViewModel(
    private val repositorio: RecordatoriosRepository,
    private val simulador: SimuladorEscenarios,
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListaUiState>(ListaUiState.Cargando)
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    private var cargaJob: Job? = null

    init {
        cargar()
    }

    fun onReintentar() = cargar()

    fun onFiltroChange(filtro: EstadoRecordatorio?) {
        _uiState.update { estado ->
            if (estado is ListaUiState.ConDatos) estado.copy(filtro = filtro) else estado
        }
    }

    fun onSimular(simulacion: Simulacion) {
        simulador.aplicar(simulacion)
        cargar()
    }

    private fun cargar() {
        cargaJob?.cancel()
        cargaJob = viewModelScope.launch {
            _uiState.update { ListaUiState.Cargando }
            repositorio.observarRecordatorios()
                .catch { error ->
                    _uiState.update {
                        ListaUiState.Error(error.message ?: "Ocurrió un error inesperado")
                    }
                }
                .collect { recordatorios ->
                    _uiState.update { anterior ->
                        if (recordatorios.isEmpty()) {
                            ListaUiState.Vacio
                        } else {
                            ListaUiState.ConDatos(
                                recordatorios = recordatorios,
                                filtro = (anterior as? ListaUiState.ConDatos)?.filtro,
                            )
                        }
                    }
                }
        }
    }
}
