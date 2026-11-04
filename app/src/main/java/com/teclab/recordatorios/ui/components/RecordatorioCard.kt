package com.teclab.recordatorios.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.teclab.recordatorios.data.RecordatoriosMock
import com.teclab.recordatorios.model.EstadoRecordatorio
import com.teclab.recordatorios.model.Recordatorio
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme
import com.teclab.recordatorios.ui.theme.colorDe
import com.teclab.recordatorios.ui.theme.colorDeEstado
import com.teclab.recordatorios.ui.theme.colorDePrioridad

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RecordatorioCard(recordatorio: Recordatorio, modifier: Modifier = Modifier) {
    val colorCategoria = colorDe(recordatorio.categoria)
    val completado = recordatorio.estado == EstadoRecordatorio.Completado

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(colorCategoria),
            )
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = recordatorio.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (completado) TextDecoration.LineThrough else null,
                )

                recordatorio.descripcion?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                recordatorio.ubicacion?.let {
                    Text(
                        text = "📍 $it",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Etiqueta(
                        texto = "${RecordatoriosMock.etiquetaDe(recordatorio.categoria)} ${recordatorio.categoria.nombre}",
                        color = colorCategoria,
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
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RecordatorioCardPreview() {
    RecordatoriosTheme {
        RecordatorioCard(
            recordatorio = RecordatoriosMock.recordatorios.first(),
            modifier = Modifier.padding(16.dp),
        )
    }
}
