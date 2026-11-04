package com.teclab.recordatorios.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teclab.recordatorios.data.RecordatoriosMock
import com.teclab.recordatorios.model.Recordatorio
import com.teclab.recordatorios.ui.AppViewModelProvider
import com.teclab.recordatorios.ui.components.CargandoContenido
import com.teclab.recordatorios.ui.components.ErrorContenido
import com.teclab.recordatorios.ui.components.Etiqueta
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme
import com.teclab.recordatorios.ui.theme.colorDe
import com.teclab.recordatorios.ui.theme.colorDeEstado
import com.teclab.recordatorios.ui.theme.colorDePrioridad

@Composable
fun DetalleRecordatorioRoute(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetalleRecordatorioViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DetalleRecordatorioScreen(
        uiState = uiState,
        onVolver = onVolver,
        onReintentar = viewModel::onReintentar,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleRecordatorioScreen(
    uiState: DetalleUiState,
    onVolver: () -> Unit,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        val contenidoModifier = Modifier.padding(innerPadding)

        when (uiState) {
            DetalleUiState.Cargando -> CargandoContenido(modifier = contenidoModifier)
            is DetalleUiState.Encontrado -> DetalleContenido(
                recordatorio = uiState.recordatorio,
                modifier = contenidoModifier,
            )
            DetalleUiState.NoEncontrado -> NoEncontradoContenido(
                onVolver = onVolver,
                modifier = contenidoModifier,
            )
            is DetalleUiState.Error -> ErrorContenido(
                mensaje = uiState.mensaje,
                onReintentar = onReintentar,
                modifier = contenidoModifier,
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetalleContenido(recordatorio: Recordatorio, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(recordatorio.titulo, style = MaterialTheme.typography.headlineSmall)

                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Etiqueta(
                        texto = "${RecordatoriosMock.etiquetaDe(recordatorio.categoria)} ${recordatorio.categoria.nombre}",
                        color = colorDe(recordatorio.categoria),
                    )
                    Etiqueta(
                        texto = "Prioridad ${recordatorio.prioridad.etiqueta}",
                        color = colorDePrioridad(recordatorio.prioridad),
                    )
                    Etiqueta(
                        texto = recordatorio.estado.etiqueta,
                        color = colorDeEstado(recordatorio.estado),
                    )
                }

                HorizontalDivider()

                CampoDetalle("Descripción", recordatorio.descripcion ?: "Sin descripción")
                CampoDetalle("Categoría", recordatorio.categoria.nombre)
                CampoDetalle("Prioridad", recordatorio.prioridad.etiqueta)
                CampoDetalle("Estado", recordatorio.estado.etiqueta)
                CampoDetalle("Ubicación", recordatorio.ubicacion ?: "Sin ubicación asignada")
                CampoDetalle("Identificador", "#${recordatorio.id}")
            }
        }
    }
}

@Composable
private fun CampoDetalle(etiqueta: String, valor: String) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(valor, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun NoEncontradoContenido(onVolver: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("No se encontró el recordatorio", style = MaterialTheme.typography.titleMedium)
        Button(onClick = onVolver) { Text("Volver al listado") }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun DetallePreview() {
    RecordatoriosTheme {
        DetalleRecordatorioScreen(
            uiState = DetalleUiState.Encontrado(RecordatoriosMock.recordatorios[1]),
            onVolver = {},
            onReintentar = {},
        )
    }
}
