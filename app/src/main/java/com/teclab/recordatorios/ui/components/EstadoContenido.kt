package com.teclab.recordatorios.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme

@Composable
fun CargandoContenido(
    modifier: Modifier = Modifier,
    mensaje: String = "Cargando…",
) {
    ContenedorCentrado(modifier) {
        CircularProgressIndicator()
        Text(mensaje, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun VacioContenido(
    mensaje: String,
    textoAccion: String,
    onAccion: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ContenedorCentrado(modifier) {
        Text("📝", style = MaterialTheme.typography.displayMedium)
        Text(
            text = mensaje,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onAccion) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text(textoAccion)
        }
    }
}

@Composable
fun ErrorContenido(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ContenedorCentrado(modifier.semantics { liveRegion = LiveRegionMode.Polite }) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp),
        )
        Text("Algo salió mal", style = MaterialTheme.typography.titleMedium)
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        Button(onClick = onReintentar) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Reintentar")
        }
    }
}

@Composable
private fun ContenedorCentrado(
    modifier: Modifier = Modifier,
    contenido: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        contenido()
    }
}

@Preview(showBackground = true)
@Composable
private fun VacioContenidoPreview() {
    RecordatoriosTheme {
        VacioContenido("Todavía no tenés recordatorios", "Crear recordatorio", onAccion = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorContenidoPreview() {
    RecordatoriosTheme {
        ErrorContenido("No se pudieron cargar los recordatorios.", onReintentar = {})
    }
}
