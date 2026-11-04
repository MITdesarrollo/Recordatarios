package com.teclab.recordatorios.model

/**
 * Estado de la PANTALLA (no de un recordatorio).
 * La UI sólo puede estar en uno de estos casos a la vez, lo que evita
 * combinaciones inválidas como "cargando y con error al mismo tiempo".
 */
sealed interface RecordatoriosUiState {
    data object Cargando : RecordatoriosUiState
    data object Vacio : RecordatoriosUiState
    data class Exito(val recordatorios: List<Recordatorio>) : RecordatoriosUiState
    data class Error(val mensaje: String) : RecordatoriosUiState
}
