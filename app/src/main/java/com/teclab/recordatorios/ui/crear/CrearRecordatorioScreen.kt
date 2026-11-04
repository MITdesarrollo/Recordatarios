package com.teclab.recordatorios.ui.crear

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.teclab.recordatorios.model.Categoria
import com.teclab.recordatorios.model.OpcionesRecordatorio
import com.teclab.recordatorios.model.Prioridad
import com.teclab.recordatorios.model.ValidacionRecordatorio
import com.teclab.recordatorios.ui.AppViewModelProvider
import com.teclab.recordatorios.ui.components.CampoTextoValidado
import com.teclab.recordatorios.ui.components.SelectorOpciones
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme

@Composable
fun CrearRecordatorioRoute(
    onGuardado: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CrearRecordatorioViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val onGuardadoActual by rememberUpdatedState(onGuardado)

    LaunchedEffect(uiState.envio) {
        if (uiState.envio is EstadoEnvio.Exito) onGuardadoActual()
    }

    CrearRecordatorioScreen(
        uiState = uiState,
        onTituloChange = viewModel::onTituloChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onCategoriaChange = viewModel::onCategoriaChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onGuardar = viewModel::onGuardar,
        onCancelar = onCancelar,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecordatorioScreen(
    uiState: CrearRecordatorioUiState,
    onTituloChange: (String) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onCategoriaChange: (Categoria) -> Unit,
    onPrioridadChange: (Prioridad) -> Unit,
    onGuardar: () -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val habilitado = !uiState.enviando

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nuevo recordatorio") },
                navigationIcon = {
                    IconButton(onClick = onCancelar, enabled = habilitado) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Cancelar y volver")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CampoTextoValidado(
                valor = uiState.titulo,
                onValorChange = onTituloChange,
                etiqueta = "Título *",
                mensajeError = uiState.errorTitulo,
                ayuda = "Mínimo ${ValidacionRecordatorio.TITULO_MIN} caracteres",
                enabled = habilitado,
                singleLine = true,
            )

            CampoTextoValidado(
                valor = uiState.descripcion,
                onValorChange = onDescripcionChange,
                etiqueta = "Descripción (opcional)",
                mensajeError = uiState.errorDescripcion,
                contador = "${uiState.descripcion.length}/${ValidacionRecordatorio.DESCRIPCION_MAX}",
                enabled = habilitado,
                minLines = 3,
            )

            SelectorOpciones(
                titulo = "Categoría *",
                opciones = OpcionesRecordatorio.categorias,
                seleccionada = uiState.categoria,
                onSeleccion = onCategoriaChange,
                textoDe = { it.nombre },
                enabled = habilitado,
            )

            SelectorOpciones(
                titulo = "Prioridad *",
                opciones = OpcionesRecordatorio.prioridades,
                seleccionada = uiState.prioridad,
                onSeleccion = onPrioridadChange,
                textoDe = { it.etiqueta },
                enabled = habilitado,
            )

            if (uiState.camposPendientes.isNotEmpty()) {
                Text(
                    text = "Para guardar, completá: ${uiState.camposPendientes.joinToString(", ")}.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }

            val envio = uiState.envio
            if (envio is EstadoEnvio.Error) {
                ErrorGuardado(mensaje = envio.mensaje)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.End),
            ) {
                OutlinedButton(onClick = onCancelar, enabled = habilitado) {
                    Text("Cancelar")
                }
                Button(onClick = onGuardar, enabled = uiState.puedeGuardar) {
                    if (uiState.enviando) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Guardando…")
                    } else {
                        Text("Guardar")
                    }
                }
            }
        }
    }
}

@Composable
private fun ErrorGuardado(mensaje: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
            contentColor = MaterialTheme.colorScheme.onErrorContainer,
        ),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.Warning, contentDescription = null)
            Column {
                Text("Error al guardar", style = MaterialTheme.typography.titleSmall)
                Text(mensaje, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CrearRecordatorioPreview() {
    RecordatoriosTheme {
        CrearRecordatorioScreen(
            uiState = CrearRecordatorioUiState(
                titulo = "Pa",
                tituloModificado = true,
                errorTitulo = "El título debe tener al menos 4 caracteres",
                categoria = Categoria.Estudio,
                camposPendientes = listOf("título", "prioridad"),
            ),
            onTituloChange = {},
            onDescripcionChange = {},
            onCategoriaChange = {},
            onPrioridadChange = {},
            onGuardar = {},
            onCancelar = {},
        )
    }
}
