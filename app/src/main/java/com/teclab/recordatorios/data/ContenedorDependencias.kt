package com.teclab.recordatorios.data

class ContenedorDependencias {
    private val repositorioEnMemoria = RecordatoriosRepositoryEnMemoria(RecordatoriosMock.recordatorios)

    val recordatoriosRepository: RecordatoriosRepository = repositorioEnMemoria
    val simuladorEscenarios: SimuladorEscenarios = repositorioEnMemoria
}
