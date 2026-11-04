package com.teclab.recordatorios.model

/**
 * Estado de un recordatorio individual.
 */
sealed class EstadoRecordatorio(val etiqueta: String) {
    data object Pendiente : EstadoRecordatorio("Pendiente")
    data object EnProgreso : EstadoRecordatorio("En progreso")
    data object Completado : EstadoRecordatorio("Completado")
    data object Vencido : EstadoRecordatorio("Vencido")
}
