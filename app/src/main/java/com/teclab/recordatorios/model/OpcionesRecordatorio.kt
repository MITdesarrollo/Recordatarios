package com.teclab.recordatorios.model

object OpcionesRecordatorio {
    val categorias: List<Categoria> = listOf(
        Categoria.Trabajo,
        Categoria.Estudio,
        Categoria.Salud,
        Categoria.Personal,
    )

    val prioridades: List<Prioridad> = listOf(Prioridad.Alta, Prioridad.Media, Prioridad.Baja)

    val estados: List<EstadoRecordatorio> = listOf(
        EstadoRecordatorio.Pendiente,
        EstadoRecordatorio.EnProgreso,
        EstadoRecordatorio.Completado,
        EstadoRecordatorio.Vencido,
    )
}
