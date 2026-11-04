package com.teclab.recordatorios.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.teclab.recordatorios.RecordatoriosApplication
import com.teclab.recordatorios.ui.crear.CrearRecordatorioViewModel
import com.teclab.recordatorios.ui.detalle.DetalleRecordatorioViewModel
import com.teclab.recordatorios.ui.lista.ListaRecordatoriosViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            ListaRecordatoriosViewModel(
                repositorio = contenedor().recordatoriosRepository,
                simulador = contenedor().simuladorEscenarios,
            )
        }
        initializer {
            CrearRecordatorioViewModel(repositorio = contenedor().recordatoriosRepository)
        }
        initializer {
            DetalleRecordatorioViewModel(
                savedStateHandle = createSavedStateHandle(),
                repositorio = contenedor().recordatoriosRepository,
            )
        }
    }
}

private fun CreationExtras.contenedor() =
    (this[APPLICATION_KEY] as RecordatoriosApplication).contenedor
