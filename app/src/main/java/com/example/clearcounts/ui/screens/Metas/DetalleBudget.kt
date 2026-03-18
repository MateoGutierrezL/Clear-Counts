package com.example.clearcounts.ui.screens.Metas

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.data.local.database.entities.BudgetEntity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DetalleBudget(
    budget: BudgetEntity,
    onVolver: () -> Unit,
    onEditar: (BudgetEntity) -> Unit,
    viewModel: MetasViewModel = hiltViewModel()
) {

    val context = LocalContext.current

    val formatoFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formatoMostrar = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es"))

    var mostrarDialogo by remember { mutableStateOf(false) }
    var tipoOperacion by remember { mutableStateOf("") }
    var campoEdicion by remember { mutableStateOf("") }
    var valorIngresado by remember { mutableStateOf("") }

    fun formatearFecha(fecha: String?): String? {
        if (fecha.isNullOrBlank()) return null
        return try {
            LocalDate.parse(fecha, formatoFecha).format(formatoMostrar)
        } catch (e: Exception) { null }
    }

    val fechaInicio = formatearFecha(budget.fechaInicio)
    val fechaLimite = formatearFecha(budget.fechaLimite)

    Scaffold(
        bottomBar = {
            Button(
                onClick = { onVolver() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                )
            ) {
                Text("OK", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onVolver,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.volver)
                    )
                }

                TextButton(
                    onClick = { onEditar(budget) },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.editar),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = budget.nombre,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (!budget.nota.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = budget.nota,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.cantidad_acumulada),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = budget.cantidadAcumulada.formatMonto(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                campoEdicion = "acumulada"
                                tipoOperacion = "sumar"
                                valorIngresado = ""
                                mostrarDialogo = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            border = BorderStroke(1.dp, color = Color(0xFF2196F3)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF2196F3))
                        }
                        Button(
                            onClick = {
                                campoEdicion = "acumulada"
                                tipoOperacion = "restar"
                                valorIngresado = ""
                                mostrarDialogo = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            border = BorderStroke(1.dp, color = Color(0xFFE74C3C)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFE74C3C))
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = stringResource(R.string.cantidad_meta),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = budget.cantidadRequerida.formatMonto(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Button(
                            onClick = {
                                campoEdicion = "requerida"
                                tipoOperacion = "sumar"
                                valorIngresado = ""
                                mostrarDialogo = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            border = BorderStroke(1.dp, color = Color(0xFF2196F3)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF2196F3))
                        }
                        Button(
                            onClick = {
                                campoEdicion = "requerida"
                                tipoOperacion = "restar"
                                valorIngresado = ""
                                mostrarDialogo = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(32.dp),
                            border = BorderStroke(1.dp, color = Color(0xFFE74C3C)),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFFE74C3C))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FilaDetalle(
                label = stringResource(R.string.fecha_simple),
                valor = fechaInicio
            )

            if (!budget.prestador.isNullOrBlank()) {
                FilaDetalle(
                    label = stringResource(R.string.prestador),
                    valor = budget.prestador,
                    esTextoSimple = true
                )
            }

            FilaDetalle(
                label = stringResource(R.string.fecha_l_mite),
                valor = fechaLimite
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.eliminarBudget(budget)
                        onVolver()
                    },
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainerLow,
                            RoundedCornerShape(12.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.eliminar),
                        tint = Color(0xFFE74C3C)
                    )
                }

                Button(
                    onClick = {
                        viewModel.marcarComoHecho(budget)
                        onVolver()
                              },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                ) {
                    Text(
                        text = stringResource(R.string.marcar_como_completado),
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }

    if (mostrarDialogo) {
        DialogoActualizarCantidad(
            tipoOperacion = tipoOperacion,
            valorIngresado = valorIngresado,
            onValorChange = { valorIngresado = it },
            onConfirmar = {
                val cantidad = valorIngresado.toDoubleOrNull()
                if (cantidad != null && cantidad > 0) {
                    when (campoEdicion) {
                        "acumulada" -> viewModel.actualizarCantidadAcumulada(
                            budget = budget,
                            cantidad = cantidad,
                            sumar = tipoOperacion == "sumar"
                        )
                        "requerida" -> viewModel.actualizarCantidadRequerida(
                            budget = budget,
                            cantidad = cantidad,
                            sumar = tipoOperacion == "sumar"
                        )
                    }

                    val mensaje = if (tipoOperacion == "sumar")
                        context.getString(R.string.se_sumaron, cantidad.formatMonto())
                    else
                        context.getString(R.string.se_restaron, cantidad.formatMonto())

                    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
                    mostrarDialogo = false
                }
            },
            onDismiss = { mostrarDialogo = false }
        )
    }

}

@Composable
fun FilaDetalle(
    label: String,
    valor: String?,
    esTextoSimple: Boolean = false
) {
    if (valor.isNullOrBlank()) return

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
        )

        if (esTextoSimple) {
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        } else {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        RoundedCornerShape(50)
                    )
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = valor,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun DialogoActualizarCantidad(
    tipoOperacion: String,
    valorIngresado: String,
    onValorChange: (String) -> Unit,
    onConfirmar: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (tipoOperacion == "sumar") stringResource(R.string.sumar_cantidad) else stringResource(
                    R.string.restar_cantidad
                ),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                Text(
                    text = if (tipoOperacion == "sumar")
                        stringResource(R.string.cu_nto_deseas_sumar_a_la_cantidad)
                    else
                        stringResource(R.string.cu_nto_deseas_restar_de_la_cantidad),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = valorIngresado,
                    onValueChange = onValorChange,
                    placeholder = { Text("Ej. 50000") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirmar,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (tipoOperacion == "sumar") Color(0xFF2196F3) else Color(0xFFE74C3C)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = if (tipoOperacion == "sumar") stringResource(R.string.sumar) else stringResource(
                        R.string.restar
                    ),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}