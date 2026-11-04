package com.teclab.recordatorios.model

/**
 * Categoría de un recordatorio.
 * Las categorías fijas son objetos únicos; `Otra` permite que el usuario
 * defina una categoría propia, validando que su nombre no esté vacío.
 */
sealed class Categoria(val nombre: String) {
    data object Trabajo : Categoria("Trabajo")
    data object Estudio : Categoria("Estudio")
    data object Salud : Categoria("Salud")
    data object Personal : Categoria("Personal")

    data class Otra(val nombrePersonalizado: String) : Categoria(nombrePersonalizado) {
        init {
            require(nombrePersonalizado.isNotBlank()) { "El nombre de la categoría no puede estar vacío" }
        }
    }
}
