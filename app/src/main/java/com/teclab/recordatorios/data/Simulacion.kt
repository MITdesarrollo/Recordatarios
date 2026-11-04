package com.teclab.recordatorios.data

enum class Simulacion(val etiqueta: String) {
    DatosDeEjemplo("Datos de ejemplo"),
    SinRecordatorios("Sin recordatorios"),
    ErrorAlCargar("Error al cargar"),
    ErrorAlGuardar("Error al guardar"),
}

interface SimuladorEscenarios {
    fun aplicar(simulacion: Simulacion)
}

class ErrorSimulado(mensaje: String) : Exception(mensaje)
