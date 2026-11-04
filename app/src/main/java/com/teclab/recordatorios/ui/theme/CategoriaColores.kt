package com.teclab.recordatorios.ui.theme

import androidx.compose.ui.graphics.Color
import com.teclab.recordatorios.model.Categoria

/**
 * Map que asocia cada categoría con un color.
 * Vive en la capa de UI (no en model/) porque Color es un tipo de Compose:
 * así el modelo de dominio no depende del framework de interfaz.
 */
val coloresPorCategoria: Map<Categoria, Color> = mapOf(
    Categoria.Trabajo to Color(0xFF1E88E5),
    Categoria.Estudio to Color(0xFF8E24AA),
    Categoria.Salud to Color(0xFF43A047),
    Categoria.Personal to Color(0xFFFB8C00),
)

private val colorCategoriaPorDefecto = Color(0xFF757575)

fun colorDe(categoria: Categoria): Color =
    coloresPorCategoria[categoria] ?: colorCategoriaPorDefecto
