package com.teclab.recordatorios.model

/**
 * Prioridad de un recordatorio.
 * Al ser sealed, el compilador conoce TODAS las prioridades posibles:
 * un `when` sobre Prioridad no necesita rama `else`.
 */
sealed class Prioridad(val nivel: Int, val etiqueta: String) {
    data object Alta : Prioridad(nivel = 3, etiqueta = "Alta")
    data object Media : Prioridad(nivel = 2, etiqueta = "Media")
    data object Baja : Prioridad(nivel = 1, etiqueta = "Baja")
}
