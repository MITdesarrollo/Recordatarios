package com.teclab.recordatorios.ui.navigation

sealed class Destino(val ruta: String) {
    data object Listado : Destino("listado")
    data object Crear : Destino("crear")
    data object Detalle : Destino("detalle/{id}") {
        const val ARG_ID = "id"
        fun conId(id: Int): String = "detalle/$id"
    }
}
