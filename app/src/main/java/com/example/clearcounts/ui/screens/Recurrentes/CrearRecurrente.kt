package com.example.clearcounts.ui.screens.Recurrentes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.example.clearcounts.ui.screens.Barras.PaymentMethodViewModel
import com.example.clearcounts.ui.screens.Categorias.CategoriasViewModel
import com.example.clearcounts.ui.screens.IngresosGastos.BotonesInferiores
import com.example.clearcounts.utils.PaymentMethodTranslator
import com.example.clearcounts.utils.PaymentMethodTranslator.getNombreTraducido

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearRecurrente(
    botonVolver: () -> Unit,
    viewModel: RecurringViewModel = hiltViewModel(),
    paymentViewModel: PaymentMethodViewModel = hiltViewModel(),
    categoriasViewModel: CategoriasViewModel = hiltViewModel()
) {
    val metodosPago by paymentViewModel.metodosPago.collectAsState()
    var metodoPago by remember { mutableStateOf("Efectivo") }
    val ingresos by categoriasViewModel.ingresos.collectAsState()
    val gastos by categoriasViewModel.gastos.collectAsState()
    var expandidoMetodoPago by remember { mutableStateOf(false) }
    var expandidoCategoria by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var diaDelMes by remember { mutableStateOf("1") }
    var tipo by remember { mutableStateOf("gasto") } // "ingreso" o "gasto"

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = botonVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = stringResource(R.string.nuevo_recurrente),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            }
        }

        // Tipo: Ingreso o Gasto
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surfaceContainerLow,
                        RoundedCornerShape(50.dp)
                    )
                    .padding(4.dp)
            ) {
                listOf("ingreso", "gasto").forEach { opcion ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50.dp))
                            .background(
                                when {
                                    opcion == "ingreso" && tipo == "ingreso" -> Color(0xFF2ECC71)
                                    opcion == "gasto" && tipo == "gasto" -> Color(0xFFE74C3C)
                                    else -> Color.Transparent
                                }
                            )
                            .clickable { tipo = opcion }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = if (opcion == "ingreso") stringResource(R.string.ingreso) else stringResource(R.string.gasto),
                            color = if (tipo == opcion) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Nombre
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.nombre), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text(stringResource(R.string.ej_netflix)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }

        // Cantidad
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.cantidad), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                placeholder = { Text("0") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        // Categoría
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.categor_a_recurrente), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            val categoriasFiltradas = if (tipo == "ingreso") ingresos else gastos

            ExposedDropdownMenuBox(
                expanded = expandidoCategoria,
                onExpandedChange = { expandidoCategoria = !expandidoCategoria }
            ) {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = {},
                    readOnly = true,
                    placeholder = { Text(stringResource(R.string.ej_entretenimiento)) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCategoria)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = expandidoCategoria,
                    onDismissRequest = { expandidoCategoria = false }
                ) {
                    categoriasFiltradas.forEach { cat ->
                        val iconoId = context.resources.getIdentifier(
                            cat.icono, "drawable", context.packageName
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (iconoId != 0) {
                                        Icon(
                                            painter = painterResource(id = iconoId),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(text = cat.nombre)
                                }
                            },
                            onClick = {
                                categoria = cat.nombre
                                expandidoCategoria = false
                            }
                        )
                    }
                }
            }
        }

        // Día del mes
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.dia_del_mes), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = diaDelMes,
                onValueChange = {
                    val num = it.toIntOrNull()
                    if (num == null || num in 1..31) diaDelMes = it
                },
                placeholder = { Text("1-31") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.m_todo_de_pago), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expandidoMetodoPago,
                onExpandedChange = { expandidoMetodoPago = !expandidoMetodoPago }
            ) {
                OutlinedTextField(
                    value = with(PaymentMethodTranslator) {
                        LocalContext.current.traducirMetodoPago(metodoPago)
                    },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoMetodoPago)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )

                ExposedDropdownMenu(
                    expanded = expandidoMetodoPago,
                    onDismissRequest = { expandidoMetodoPago = false }
                ) {
                    metodosPago.forEach { metodo ->
                        val iconoId = context.resources.getIdentifier(
                            metodo.icono, "drawable", context.packageName
                        )
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (iconoId != 0) {
                                        Icon(
                                            painter = painterResource(id = iconoId),
                                            contentDescription = null,
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                    }
                                    Text(text = metodo.getNombreTraducido())
                                }
                            },
                            onClick = {
                                metodoPago = metodo.nombre
                                expandidoMetodoPago = false
                            }
                        )
                    }
                }
            }
        }

        // Botones
        item {
            Spacer(modifier = Modifier.height(32.dp))
            BotonesInferiores(
                onCancelChange = botonVolver,
                onCreateChange = {
                    if (nombre.isNotBlank() && cantidad.isNotBlank()) {
                        viewModel.guardar(
                            RecurringEntity(
                                nombre = nombre,
                                categoria = categoria,
                                cantidad = cantidad.toDoubleOrNull() ?: 0.0,
                                tipo = tipo,
                                diaDelMes = diaDelMes.toIntOrNull() ?: 1,
                                metodoPago = metodoPago
                            )
                        )
                        botonVolver()
                    }
                }
            )
        }
    }
}