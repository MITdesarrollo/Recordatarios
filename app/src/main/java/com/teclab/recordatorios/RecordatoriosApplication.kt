package com.teclab.recordatorios

import android.app.Application
import com.teclab.recordatorios.data.ContenedorDependencias

class RecordatoriosApplication : Application() {
    val contenedor: ContenedorDependencias by lazy { ContenedorDependencias() }
}
