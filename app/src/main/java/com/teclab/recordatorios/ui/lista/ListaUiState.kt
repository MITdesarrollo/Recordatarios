package com.teclab.recordatorios.ui.lista

import com.teclab.recordatorios.data.cantidadPorPrioridad
import com.teclab.recordatorios.data.porEstado
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.Recordatorio

sealed interface ListaUiState {
    data object Cargando : ListaUiState

    data object Vacio : ListaUiState

    data class ConDatos(
        val recordatorios: List<Recordatorio>,
        val filtro: EstadoRecordatorio? = null,
    ) : ListaUiState {
        val visibles: List<Recordatorio> = filtro?.let { recordatorios.porEstado(it) } ?: recordatorios
        val conteoPorPrioridad: Map<Prioridad, Int> = recordatorios.cantidadPorPrioridad()
    }

    data class Error(val mensaje: String) : ListaUiState
}
