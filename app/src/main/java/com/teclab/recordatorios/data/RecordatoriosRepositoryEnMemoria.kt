package com.teclab.recordatorios.data

import com.teclab.recordatorios.model.NuevoRecordatorio
import com.teclab.recordatorios.model.Recordatorio
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class RecordatoriosRepositoryEnMemoria(
    private val datosIniciales: List<Recordatorio>,
    private val demoraCargaMs: Long = 1_200,
    private val demoraGuardadoMs: Long = 1_500,
) : RecordatoriosRepository, SimuladorEscenarios {

    private val recordatorios = MutableStateFlow(datosIniciales)

    @Volatile
    private var fallarProximaCarga = false

    @Volatile
    private var fallarProximoGuardado = false

    override fun observarRecordatorios(): Flow<List<Recordatorio>> = flow {
        delay(demoraCargaMs)
        if (fallarProximaCarga) {
            fallarProximaCarga = false
            throw ErrorSimulado("No se pudieron cargar los recordatorios. Revisá tu conexión e intentá nuevamente.")
        }
        emitAll(recordatorios)
    }

    override suspend fun obtenerPorId(id: Int): Recordatorio? {
        delay(demoraCargaMs / 2)
        return recordatorios.value.firstOrNull { it.id == id }
    }

    override suspend fun guardar(nuevo: NuevoRecordatorio): Recordatorio {
        delay(demoraGuardadoMs)
        if (fallarProximoGuardado) {
            fallarProximoGuardado = false
            throw ErrorSimulado("No se pudo guardar el recordatorio. Intentá nuevamente.")
        }
        val creado = Recordatorio(
            id = (recordatorios.value.maxOfOrNull { it.id } ?: 0) + 1,
            titulo = nuevo.titulo,
            descripcion = nuevo.descripcion,
            categoria = nuevo.categoria,
            prioridad = nuevo.prioridad,
        )
        recordatorios.update { listOf(creado) + it }
        return creado
    }

    override fun aplicar(simulacion: Simulacion) {
        when (simulacion) {
            Simulacion.DatosDeEjemplo -> recordatorios.value = datosIniciales
            Simulacion.SinRecordatorios -> recordatorios.value = emptyList()
            Simulacion.ErrorAlCargar -> fallarProximaCarga = true
            Simulacion.ErrorAlGuardar -> fallarProximoGuardado = true
        }
    }
}
