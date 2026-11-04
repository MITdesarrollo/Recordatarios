package com.teclab.recordatorios.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teclab.recordatorios.data.RecordatoriosMock
import com.teclab.recordatorios.data.cantidadPorPrioridad
import com.teclab.recordatorios.data.porEstado
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.Recordatorio
import com.teclab.recordatorios.model.RecordatoriosUiState
import com.teclab.recordatorios.ui.components.Etiqueta
import com.teclab.recordatorios.ui.components.RecordatorioCard
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme
import com.teclab.recordatorios.ui.theme.colorDePrioridad

private val estadosFiltrables = listOf(
    EstadoRecordatorio.Pendiente,
    EstadoRecordatorio.EnProgreso,
    EstadoRecordatorio.Completado,
    EstadoRecordatorio.Vencido,
)

private val prioridades = listOf(Prioridad.Alta, Prioridad.Media, Prioridad.Baja)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordatoriosScreen(uiState: RecordatoriosUiState, modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
    ) { innerPadding ->
        val contenidoModifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        when (uiState) {
            RecordatoriosUiState.Cargando -> Box(contenidoModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            RecordatoriosUiState.Vacio -> MensajeCentrado("Todavía no tenés recordatorios", contenidoModifier)
            is RecordatoriosUiState.Error -> MensajeCentrado(uiState.mensaje, contenidoModifier)
            is RecordatoriosUiState.Exito -> ListaRecordatorios(uiState.recordatorios, contenidoModifier)
        }
    }
}

@Composable
private fun ListaRecordatorios(recordatorios: List<Recordatorio>, modifier: Modifier = Modifier) {
    var filtro by remember { mutableStateOf<EstadoRecordatorio?>(null) }
    val visibles = filtro?.let { recordatorios.porEstado(it) } ?: recordatorios

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ResumenPrioridades(recordatorios.cantidadPorPrioridad()) }
        item { FiltrosEstado(filtro, onFiltroChange = { filtro = it }) }

        if (visibles.isEmpty()) {
            item { Text("No hay recordatorios con este estado") }
        }
        items(visibles, key = { it.id }) { recordatorio ->
            RecordatorioCard(recordatorio)
        }
    }
}

@Composable
private fun ResumenPrioridades(conteo: Map<Prioridad, Int>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Resumen por prioridad", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            prioridades.forEach { prioridad ->
                Etiqueta(
                    texto = "${prioridad.etiqueta}: ${conteo[prioridad] ?: 0}",
                    color = colorDePrioridad(prioridad),
                )
            }
        }
    }
}

@Composable
private fun FiltrosEstado(
    seleccionado: EstadoRecordatorio?,
    onFiltroChange: (EstadoRecordatorio?) -> Unit,
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            selected = seleccionado == null,
            onClick = { onFiltroChange(null) },
            label = { Text("Todos") },
        )
        estadosFiltrables.forEach { estado ->
            FilterChip(
                selected = seleccionado == estado,
                onClick = { onFiltroChange(if (seleccionado == estado) null else estado) },
                label = { Text(estado.etiqueta) },
            )
        }
    }
}

@Composable
private fun MensajeCentrado(texto: String, modifier: Modifier = Modifier) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(texto, style = MaterialTheme.typography.bodyLarge)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun RecordatoriosScreenPreview() {
    RecordatoriosTheme {
        RecordatoriosScreen(RecordatoriosUiState.Exito(RecordatoriosMock.recordatorios))
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordatoriosScreenVacioPreview() {
    RecordatoriosTheme {
        RecordatoriosScreen(RecordatoriosUiState.Vacio)
    }
}
