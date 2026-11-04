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
        require(titulo.isNotBlank()) { "El título no puede estar vacío" }
        require(titulo.length <= TITULO_MAX) { "El título no puede superar los $TITULO_MAX caracteres" }
        require(descripcion == null || descripcion.isNotBlank()) { "La descripción, si existe, no puede estar vacía" }
    }

    val tieneUbicacion: Boolean get() = ubicacion != null

    companion object {
        const val TITULO_MAX = 50

        fun crear(
            id: Int,
            titulo: String,
            categoria: Categoria,
            prioridad: Prioridad,
            estado: EstadoRecordatorio = EstadoRecordatorio.Pendiente,
            descripcion: String? = null,
            ubicacion: String? = null,
        ): Result<Recordatorio> = runCatching {
            Recordatorio(id, titulo.trim(), categoria, prioridad, estado, descripcion, ubicacion)
        }
    }
}
