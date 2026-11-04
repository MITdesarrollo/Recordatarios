package com.teclab.recordatorios.ui.theme

import androidx.compose.ui.graphics.Color
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Prioridad

fun colorDePrioridad(prioridad: Prioridad): Color = when (prioridad) {
    Prioridad.Alta -> Color(0xFFE53935)
    Prioridad.Media -> Color(0xFFF9A825)
    Prioridad.Baja -> Color(0xFF43A047)
}

fun colorDeEstado(estado: EstadoRecordatorio): Color = when (estado) {
    EstadoRecordatorio.Pendiente -> Color(0xFF546E7A)
    EstadoRecordatorio.EnProgreso -> Color(0xFF1E88E5)
    EstadoRecordatorio.Completado -> Color(0xFF43A047)
    EstadoRecordatorio.Vencido -> Color(0xFFE53935)
}
