package com.teclab.recordatorios.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics

@Composable
fun CampoTextoValidado(
    valor: String,
    onValorChange: (String) -> Unit,
    etiqueta: String,
    modifier: Modifier = Modifier,
    mensajeError: String? = null,
    ayuda: String? = null,
    contador: String? = null,
    enabled: Boolean = true,
    singleLine: Boolean = false,
    minLines: Int = 1,
) {
    val hayError = mensajeError != null

    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(etiqueta) },
        isError = hayError,
        enabled = enabled,
        singleLine = singleLine,
        minLines = minLines,
        trailingIcon = if (hayError) {
            { Icon(Icons.Default.Warning, contentDescription = null) }
        } else {
            null
        },
        supportingText = {
            Row {
                Text(
                    text = mensajeError ?: ayuda.orEmpty(),
                    modifier = Modifier.weight(1f),
                )
                contador?.let { Text(it) }
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .semantics { if (mensajeError != null) error(mensajeError) },
    )
}
