package com.teclab.recordatorios.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teclab.recordatorios.ui.crear.CrearRecordatorioRoute
import com.teclab.recordatorios.ui.detalle.DetalleRecordatorioRoute
import com.teclab.recordatorios.ui.lista.ListaRecordatoriosRoute

@Composable
fun RecordatoriosApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    RecordatoriosNavHost(navController = navController, modifier = modifier)
}

@Composable
fun RecordatoriosNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Destino.Listado.ruta,
        modifier = modifier,
    ) {
        composable(Destino.Listado.ruta) {
            ListaRecordatoriosRoute(
                onCrearRecordatorio = {
                    navController.navigate(Destino.Crear.ruta) { launchSingleTop = true }
                },
                onRecordatorioClick = { id ->
                    navController.navigate(Destino.Detalle.conId(id)) { launchSingleTop = true }
                },
            )
        }

        composable(Destino.Crear.ruta) {
            CrearRecordatorioRoute(
                onGuardado = { navController.volver() },
                onCancelar = { navController.volver() },
            )
        }

        composable(
            route = Destino.Detalle.ruta,
            arguments = listOf(navArgument(Destino.Detalle.ARG_ID) { type = NavType.IntType }),
        ) {
            DetalleRecordatorioRoute(onVolver = { navController.volver() })
        }
    }
}

private fun NavHostController.volver() {
    if (previousBackStackEntry != null) popBackStack()
}
