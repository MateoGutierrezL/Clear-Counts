package com.example.clearcounts.ui.screens.Recurrentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarViewDay
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ViewWeek
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.clearcounts.utils.CategoryTranslator.getNombreTraducido
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
    var diaReferencia by remember { mutableStateOf(1) }
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
    var frecuencia by remember { mutableStateOf("mensual") }
    var tipo by remember { mutableStateOf("gasto") }
    var nombreError by remember { mutableStateOf(false) }
    var cantidadError by remember { mutableStateOf(false) }
    var categoriaError by remember { mutableStateOf(false) }
//DropdownMenuItem
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
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                placeholder = { Text(if (tipo == "gasto")stringResource(R.string.ej_netflix) else stringResource(R.string.ej_salario)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = nombreError,
                supportingText = {
                    AnimatedVisibility(visible = nombreError) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.nombre_requerido),
                                color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        }
                    }
                }
            )
        }

        // Cantidad
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.cantidad), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = cantidad,
                onValueChange = { nuevaEntrada ->
                    val soloNumeros = nuevaEntrada.filter { it.isDigit() }
                    if (soloNumeros.length <= 20) {
                        cantidad = soloNumeros
                        cantidadError = false
                    }
                },
                placeholder = { Text("0") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = cantidadError,
                supportingText = {
                    AnimatedVisibility(visible = cantidadError) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(stringResource(R.string.cantidad_requerida),
                                color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                        }
                    }
                }
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
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    isError = categoriaError,
                    supportingText = {
                        AnimatedVisibility(visible = categoriaError) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(stringResource(R.string.categoria_requerida),
                                    color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                            }
                        }
                    }
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
                                    Text(text = cat.getNombreTraducido())
                                }
                            },
                            onClick = {
                                categoria = cat.nombre
                                categoriaError = false
                                expandidoCategoria = false
                            }
                        )
                    }
                }
            }
        }

        // selector de frecuencia
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.frecuencia),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val opciones = listOf(
                "diario" to stringResource(R.string.frecuencia_diario),
                "semanal" to stringResource(R.string.frecuencia_semanal),
                "quincenal" to stringResource(R.string.frecuencia_quincenal),
                "mensual" to stringResource(R.string.frecuencia_mensual)
            )

            // Grid 2x2 de chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                opciones.chunked(2).forEach { fila ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        fila.forEach { (valor, label) ->
                            val seleccionado = frecuencia == valor
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (seleccionado) MaterialTheme.colorScheme.primaryContainer
                                        else MaterialTheme.colorScheme.surfaceContainerLow
                                    )
                                    .border(
                                        width = if (seleccionado) 2.dp else 1.dp,
                                        color = if (seleccionado) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .clickable { frecuencia = valor }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = when (valor) {
                                            "diario" -> Icons.Default.CalendarViewDay
                                            "semanal" -> Icons.Default.ViewWeek
                                            "quincenal" -> Icons.Default.DateRange
                                            else -> Icons.Default.CalendarMonth
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp),
                                        tint = if (seleccionado) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = label,
                                        fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                        color = if (seleccionado) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                        // Si la fila tiene solo 1 elemento, rellena el espacio
                        if (fila.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
        item {
            SelectorDiaReferencia(
                frecuencia = frecuencia,
                diaReferencia = diaReferencia,
                onDiaReferenciaChange = { diaReferencia = it }
            )
        }


        // metodo de pago
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
                    nombreError = nombre.isBlank()
                    cantidadError = cantidad.isBlank() || cantidad.toDoubleOrNull() == null
                    categoriaError = categoria.isBlank()

                    if (!nombreError && !cantidadError && !categoriaError) {
                        viewModel.guardar(RecurringEntity(
                            nombre = nombre,
                            categoria = categoria,
                            cantidad = cantidad.toDoubleOrNull() ?: 0.0,
                            tipo = tipo,
                            frecuencia = frecuencia,
                            diaReferencia = diaReferencia,
                            metodoPago = metodoPago
                        ))
                        botonVolver()
                        true
                    } else {
                        false 
                    }
                }
            )
        }
    }
}



@Composable
fun SelectorDiaReferencia(
    frecuencia: String,
    diaReferencia: Int,
    onDiaReferenciaChange: (Int) -> Unit
) {
    // Diario no necesita configuración
    if (frecuencia == "diario") return

    Spacer(modifier = Modifier.height(16.dp))

    when (frecuencia) {
        "mensual", "quincenal" -> {
            Text(
                text = if (frecuencia == "mensual")
                    stringResource(R.string.dia_del_mes)
                else
                    stringResource(R.string.dia_inicio_quincenal),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Grid de días 1-31
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(31) { index ->
                    val dia = index + 1
                    val seleccionado = diaReferencia == dia
                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                if (seleccionado) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceContainerLow
                            )
                            .clickable { onDiaReferenciaChange(dia) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$dia",
                            fontSize = 12.sp,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                            color = if (seleccionado) Color.White
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }


            if (frecuencia == "quincenal") {
                val segundoDia = if (diaReferencia + 15 <= 31) diaReferencia + 15
                else (diaReferencia + 15) - 31
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(
                                R.string.quincenal_info,
                                diaReferencia,
                                segundoDia
                            ),
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        "semanal" -> {
            Text(
                text = stringResource(R.string.dia_de_la_semana),
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))

            val diasSemana = listOf(
                1 to stringResource(R.string.lunes),
                2 to stringResource(R.string.martes),
                3 to stringResource(R.string.miercoles),
                4 to stringResource(R.string.jueves),
                5 to stringResource(R.string.viernes),
                6 to stringResource(R.string.sabado),
                7 to stringResource(R.string.domingo)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                diasSemana.forEach { (valor, label) ->
                    val seleccionado = diaReferencia == valor
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                if (seleccionado) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.surfaceContainerLow
                            )
                            .clickable { onDiaReferenciaChange(valor) },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label.first().toString(), // L, M, X, J, V, S, D
                            fontSize = 13.sp,
                            fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                            color = if (seleccionado) Color.White
                            else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}