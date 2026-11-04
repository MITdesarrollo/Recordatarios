package com.teclab.recordatorios.model

data class Recordatorio(
    val id: Int,
    val titulo: String,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
    val descripcion: String? = null,
    val ubicacion: String? = null,
) {
    init {
        require(id > 0) { "El id debe ser positivo" }
        ValidacionRecordatorio.validarTitulo(titulo)?.let { throw IllegalArgumentException(it) }
        require(descripcion == null || descripcion.isNotBlank()) { "La descripción, si existe, no puede estar vacía" }
        descripcion?.let(ValidacionRecordatorio::validarDescripcion)?.let { throw IllegalArgumentException(it) }
    }

    val tieneUbicacion: Boolean get() = ubicacion != null
}

data class NuevoRecordatorio(
    val titulo: String,
    val descripcion: String?,
    val categoria: Categoria,
    val prioridad: Prioridad,
)
