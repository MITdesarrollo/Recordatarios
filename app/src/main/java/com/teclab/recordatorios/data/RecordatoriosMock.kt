package com.teclab.recordatorios.data

import com.teclab.recordatorios.model.Categoria
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.Recordatorio

object RecordatoriosMock {

    val recordatorios: List<Recordatorio> = listOf(
        Recordatorio(
            id = 1,
            titulo = "Entregar Actividad 1",
            categoria = Categoria.Estudio,
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.EnProgreso,
            descripcion = "Subir el PDF con capturas al campus",
        ),
        Recordatorio(
            id = 2,
            titulo = "Turno con el dentista",
            categoria = Categoria.Salud,
            prioridad = Prioridad.Media,
            ubicacion = "Consultorio Av. Belgrano 1250",
        ),
        Recordatorio(
            id = 3,
            titulo = "Reunión de equipo",
            categoria = Categoria.Trabajo,
            prioridad = Prioridad.Alta,
            descripcion = "Revisar avances del sprint",
        ),
        Recordatorio(
            id = 4,
            titulo = "Comprar regalo de cumpleaños",
            categoria = Categoria.Personal,
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Completado,
        ),
        Recordatorio(
            id = 5,
            titulo = "Pagar la luz",
            categoria = Categoria.Otra("Hogar"),
            prioridad = Prioridad.Alta,
            estado = EstadoRecordatorio.Vencido,
        ),
        Recordatorio(
            id = 6,
            titulo = "Estudiar Kotlin: colecciones",
            categoria = Categoria.Estudio,
            prioridad = Prioridad.Media,
        ),
        Recordatorio(
            id = 7,
            titulo = "Regar las plantas",
            categoria = Categoria.Otra("Hogar"),
            prioridad = Prioridad.Baja,
            estado = EstadoRecordatorio.Completado,
        ),
        Recordatorio(
            id = 8,
            titulo = "Salir a correr",
            categoria = Categoria.Salud,
            prioridad = Prioridad.Baja,
            ubicacion = "Parque Sarmiento",
        ),
    )

    val categorias: Set<Categoria> = recordatorios.categoriasUnicas()

    val etiquetasPorCategoria: Map<Categoria, String> = mapOf(
        Categoria.Trabajo to "💼",
        Categoria.Estudio to "📚",
        Categoria.Salud to "🩺",
        Categoria.Personal to "🙂",
    )

    fun etiquetaDe(categoria: Categoria): String = etiquetasPorCategoria[categoria] ?: "📌"
}
