package com.teclab.recordatorios

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.teclab.recordatorios.data.RecordatoriosMock
import com.teclab.recordatorios.model.RecordatoriosUiState
import com.teclab.recordatorios.ui.screens.RecordatoriosScreen
import com.teclab.recordatorios.ui.theme.RecordatoriosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecordatoriosTheme {
                val uiState = RecordatoriosUiState.Exito(RecordatoriosMock.recordatorios)
                RecordatoriosScreen(uiState = uiState)
            }
        }
    }
}
