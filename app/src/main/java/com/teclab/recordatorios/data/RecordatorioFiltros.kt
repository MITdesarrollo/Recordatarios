package com.teclab.recordatorios.data

import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.Recordatorio

/*
 * Funciones de extensión sobre List<Recordatorio>.
 * Ninguna modifica la lista original: todas devuelven una colección NUEVA,
 * respetando la inmutabilidad del modelo.
 */

fun List<Recordatorio>.porEstado(estado: EstadoRecordatorio): List<Recordatorio> =
    filter { it.estado == estado }

fun List<Recordatorio>.porPrioridad(prioridad: Prioridad): List<Recordatorio> =
    filter { it.prioridad == prioridad }

/** Pendientes y en progreso, de mayor a menor prioridad. */
fun List<Recordatorio>.activosOrdenados(): List<Recordatorio> =
    filter { it.estado == EstadoRecordatorio.Pendiente || it.estado == EstadoRecordatorio.EnProgreso }
        .sortedByDescending { it.prioridad.nivel }

/** Map que agrupa los recordatorios según su estado. */
fun List<Recordatorio>.agrupadosPorEstado(): Map<EstadoRecordatorio, List<Recordatorio>> =
    groupBy { it.estado }

/** Map con la cantidad de recordatorios por prioridad. */
fun List<Recordatorio>.cantidadPorPrioridad(): Map<Prioridad, Int> =
    groupingBy { it.prioridad }.eachCount()
