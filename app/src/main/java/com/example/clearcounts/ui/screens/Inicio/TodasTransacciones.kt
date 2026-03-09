package com.example.clearcounts.ui.screens.Inicio

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import com.example.clearcounts.ui.screens.Inicio.DetalleGastoPopup
import com.example.clearcounts.ui.screens.Inicio.DetalleIngresoPopup
import com.example.clearcounts.ui.screens.Inicio.ItemGasto
import com.example.clearcounts.ui.screens.Inicio.ItemIngreso


@Composable
fun TodasTransacciones(
    botonVolver: () -> Unit,
    viewModel: InicioViewModel = hiltViewModel()
) {
    val movimientosAgrupados by viewModel.movimientosAgrupadosSinLimite.collectAsState()
    var ingresoSeleccionado by remember { mutableStateOf<IncomeEntity?>(null) }
    var gastoSeleccionado by remember { mutableStateOf<ExpenseEntity?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = botonVolver) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Volver",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = "Todas las transacciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        HorizontalDivider()

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(
                items = movimientosAgrupados,
                key = { item ->
                    when (item) {
                        is InicioViewModel.MovimientoItem.Header -> "header_${item.fecha}"
                        is InicioViewModel.MovimientoItem.Transaccion -> when (val mov = item.movimiento) {
                            is IncomeEntity -> "inc_${mov.id}"
                            is ExpenseEntity -> "exp_${mov.id}"
                            else -> item.hashCode()
                        }
                    }
                }
            ) { item ->
                when (item) {
                    is InicioViewModel.MovimientoItem.Header -> {
                        Text(
                            text = formatearFechaHeader(item.fecha),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )
                    }
                    is InicioViewModel.MovimientoItem.Transaccion -> {
                        when (val mov = item.movimiento) {
                            is IncomeEntity -> ItemIngreso(
                                ingreso = mov,
                                onPopUpIngreso = { ingresoSeleccionado = mov }
                            )
                            is ExpenseEntity -> ItemGasto(
                                gasto = mov,
                                onPopUpGasto = { gastoSeleccionado = mov }
                            )
                        }
                    }
                }
            }
        }
    }

    ingresoSeleccionado?.let { ingreso ->
        DetalleIngresoPopup(
            ingreso = ingreso,
            onDismiss = { ingresoSeleccionado = null },
            onDelete = {
                viewModel.deleteIncome(ingreso)
                ingresoSeleccionado = null
            }
        )
    }

    gastoSeleccionado?.let { gasto ->
        DetalleGastoPopup(
            gasto = gasto,
            onDismiss = { gastoSeleccionado = null },
            onDelete = {
                viewModel.deleteExpense(gasto)
                gastoSeleccionado = null
            }
        )
    }
}