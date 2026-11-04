package com.teclab.recordatorios.data

import com.teclab.recordatorios.model.Categoria
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.Recordatorio

fun List<Recordatorio>.categoriasUnicas(): Set<Categoria> =
    map { it.categoria }.toSet()

fun List<Recordatorio>.porEstado(estado: EstadoRecordatorio): List<Recordatorio> =
    filter { it.estado == estado }

fun List<Recordatorio>.porPrioridad(prioridad: Prioridad): List<Recordatorio> =
    filter { it.prioridad == prioridad }

fun List<Recordatorio>.activosOrdenados(): List<Recordatorio> =
    filter { it.estado == EstadoRecordatorio.Pendiente || it.estado == EstadoRecordatorio.EnProgreso }
        .sortedByDescending { it.prioridad.nivel }

fun List<Recordatorio>.agrupadosPorEstado(): Map<EstadoRecordatorio, List<Recordatorio>> =
    groupBy { it.estado }

fun List<Recordatorio>.cantidadPorPrioridad(): Map<Prioridad, Int> =
    groupingBy { it.prioridad }.eachCount()
