package com.teclab.recordatorios.ui.lista

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teclab.recordatorios.data.RecordatoriosMock
import com.teclab.recordatorios.data.Simulacion
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.OpcionesRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.ui.AppViewModelProvider
import com.teclab.recordatorios.ui.components.CargandoContenido
import com.teclab.recordatorios.ui.components.ErrorContenido
import com.teclab.recordatorios.ui.components.Etiqueta
import com.teclab.recordatorios.ui.components.RecordatorioCard
import com.teclab.recordatorios.ui.components.VacioContenido
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme
import com.teclab.recordatorios.ui.theme.colorDePrioridad

@Composable
fun ListaRecordatoriosRoute(
    onCrearRecordatorio: () -> Unit,
    onRecordatorioClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaRecordatoriosViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ListaRecordatoriosScreen(
        uiState = uiState,
        onCrearRecordatorio = onCrearRecordatorio,
        onRecordatorioClick = onRecordatorioClick,
        onReintentar = viewModel::onReintentar,
        onFiltroChange = viewModel::onFiltroChange,
        onSimular = viewModel::onSimular,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaRecordatoriosScreen(
    uiState: ListaUiState,
    onCrearRecordatorio: () -> Unit,
    onRecordatorioClick: (Int) -> Unit,
    onReintentar: () -> Unit,
    onFiltroChange: (EstadoRecordatorio?) -> Unit,
    onSimular: (Simulacion) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Mis recordatorios") },
                actions = { MenuSimulacion(onSimular = onSimular) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
            )
        },
        floatingActionButton = {
            if (uiState is ListaUiState.ConDatos) {
                ExtendedFloatingActionButton(
                    onClick = onCrearRecordatorio,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Nuevo") },
                )
            }
        },
    ) { innerPadding ->
        val contenidoModifier = Modifier.padding(innerPadding)

        when (uiState) {
            ListaUiState.Cargando -> CargandoContenido(
                modifier = contenidoModifier,
                mensaje = "Cargando recordatorios…",
            )
            ListaUiState.Vacio -> VacioContenido(
                mensaje = "Todavía no tenés recordatorios",
                textoAccion = "Crear recordatorio",
                onAccion = onCrearRecordatorio,
                modifier = contenidoModifier,
            )
            is ListaUiState.Error -> ErrorContenido(
                mensaje = uiState.mensaje,
                onReintentar = onReintentar,
                modifier = contenidoModifier,
            )
            is ListaUiState.ConDatos -> ListaConDatos(
                uiState = uiState,
                onRecordatorioClick = onRecordatorioClick,
                onFiltroChange = onFiltroChange,
                modifier = contenidoModifier,
            )
        }
    }
}

@Composable
private fun ListaConDatos(
    uiState: ListaUiState.ConDatos,
    onRecordatorioClick: (Int) -> Unit,
    onFiltroChange: (EstadoRecordatorio?) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item { ResumenPrioridades(uiState.conteoPorPrioridad) }
        item { FiltrosEstado(seleccionado = uiState.filtro, onFiltroChange = onFiltroChange) }

        if (uiState.visibles.isEmpty()) {
            item { Text("No hay recordatorios con este estado") }
        }
        items(uiState.visibles, key = { it.id }) { recordatorio ->
            RecordatorioCard(
                recordatorio = recordatorio,
                onClick = { onRecordatorioClick(recordatorio.id) },
            )
        }
    }
}

@Composable
private fun ResumenPrioridades(conteo: Map<Prioridad, Int>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Resumen por prioridad", style = MaterialTheme.typography.titleSmall)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OpcionesRecordatorio.prioridades.forEach { prioridad ->
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
        OpcionesRecordatorio.estados.forEach { estado ->
            FilterChip(
                selected = seleccionado == estado,
                onClick = { onFiltroChange(if (seleccionado == estado) null else estado) },
                label = { Text(estado.etiqueta) },
            )
        }
    }
}

@Composable
private fun MenuSimulacion(onSimular: (Simulacion) -> Unit) {
    var expandido by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expandido = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "Simular escenarios")
        }
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            Simulacion.entries.forEach { simulacion ->
                DropdownMenuItem(
                    text = { Text(simulacion.etiqueta) },
                    onClick = {
                        expandido = false
                        onSimular(simulacion)
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ListaConDatosPreview() {
    RecordatoriosTheme {
        ListaRecordatoriosScreen(
            uiState = ListaUiState.ConDatos(RecordatoriosMock.recordatorios),
            onCrearRecordatorio = {},
            onRecordatorioClick = {},
            onReintentar = {},
            onFiltroChange = {},
            onSimular = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ListaErrorPreview() {
    RecordatoriosTheme {
        ListaRecordatoriosScreen(
            uiState = ListaUiState.Error("No se pudieron cargar los recordatorios."),
            onCrearRecordatorio = {},
            onRecordatorioClick = {},
            onReintentar = {},
            onFiltroChange = {},
            onSimular = {},
        )
    }
}
