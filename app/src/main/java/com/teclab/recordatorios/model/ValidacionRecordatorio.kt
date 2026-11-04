package com.teclab.recordatorios.model

object ValidacionRecordatorio {
    const val TITULO_MIN = 4
    const val TITULO_MAX = 50
    const val DESCRIPCION_MAX = 200

    fun validarTitulo(titulo: String): String? {
        val limpio = titulo.trim()
        return when {
            limpio.length < TITULO_MIN -> "El título debe tener al menos $TITULO_MIN caracteres"
            limpio.length > TITULO_MAX -> "El título no puede superar los $TITULO_MAX caracteres"
            else -> null
        }
    }

    fun validarDescripcion(descripcion: String): String? =
        if (descripcion.length > DESCRIPCION_MAX) {
            "La descripción no puede superar los $DESCRIPCION_MAX caracteres"
        } else {
            null
        }

    fun validarCategoria(categoria: Categoria?): String? = when {
        categoria == null -> "Seleccioná una categoría"
        categoria !in OpcionesRecordatorio.categorias -> "La categoría seleccionada no es válida"
        else -> null
    }

    fun validarPrioridad(prioridad: Prioridad?): String? =
        if (prioridad == null) "Seleccioná una prioridad" else null
}
