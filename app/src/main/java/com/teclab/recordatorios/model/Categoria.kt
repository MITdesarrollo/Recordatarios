package com.teclab.recordatorios.model

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
