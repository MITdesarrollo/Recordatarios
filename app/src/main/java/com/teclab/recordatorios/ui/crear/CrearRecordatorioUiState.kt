package com.teclab.recordatorios.ui.crear

import com.teclab.recordatorios.model.Categoria
import com.teclab.recordatorios.model.Prioridad

data class CrearRecordatorioUiState(
    val titulo: String = "",
    val descripcion: String = "",
    val categoria: Categoria? = null,
    val prioridad: Prioridad? = null,
    val tituloModificado: Boolean = false,
    val errorTitulo: String? = null,
    val errorDescripcion: String? = null,
    val camposPendientes: List<String> = emptyList(),
    val envio: EstadoEnvio = EstadoEnvio.Inactivo,
) {
    val esValido: Boolean get() = camposPendientes.isEmpty()
    val enviando: Boolean get() = envio is EstadoEnvio.Enviando
    val puedeGuardar: Boolean get() = esValido && envio !is EstadoEnvio.Enviando && envio !is EstadoEnvio.Exito
}

sealed interface EstadoEnvio {
    data object Inactivo : EstadoEnvio
    data object Enviando : EstadoEnvio
    data object Exito : EstadoEnvio
    data class Error(val mensaje: String) : EstadoEnvio
}
