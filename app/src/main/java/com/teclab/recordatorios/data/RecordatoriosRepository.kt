package com.teclab.recordatorios.data

import com.teclab.recordatorios.model.NuevoRecordatorio
import com.teclab.recordatorios.model.Recordatorio
import kotlinx.coroutines.flow.Flow

interface RecordatoriosRepository {
    fun observarRecordatorios(): Flow<List<Recordatorio>>
    suspend fun obtenerPorId(id: Int): Recordatorio?
    suspend fun guardar(nuevo: NuevoRecordatorio): Recordatorio
}
