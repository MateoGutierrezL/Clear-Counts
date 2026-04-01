package com.example.clearcounts.ui.screens.Recurrentes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.ui.screens.Metas.formatMonto
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.utils.PaymentMethodTranslator
import com.example.clearcounts.data.local.database.entities.RecurringEntity
import com.example.clearcounts.ui.screens.Barras.PaymentMethodViewModel
import com.example.clearcounts.ui.screens.Categorias.CategoriasViewModel
import com.example.clearcounts.ui.screens.IngresosGastos.BotonesInferiores
import com.example.clearcounts.utils.CategoryTranslator
import com.example.clearcounts.utils.CategoryTranslator.getNombreTraducido
import com.example.clearcounts.utils.PaymentMethodTranslator.getNombreTraducido

@Composable
fun ItemRecurrente(
    item: RecurringEntity,
    onToggle: (RecurringEntity) -> Unit,
    onEditar: (RecurringEntity) -> Unit,
    onEliminar: (RecurringEntity) -> Unit
) {
    val esIngreso = item.tipo == "ingreso"
    val iconColor = if (esIngreso) Color(0xFF2ECC71) else Color(0xFFE74C3C)
    val containerColor = iconColor.copy(alpha = 0.15f)
    val icon = if (esIngreso) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }


    val diasSemana = listOf("", "Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
    val textoFrecuencia = when (item.frecuencia) {
        "diario" -> stringResource(R.string.frecuencia_diario)
        "semanal" -> "${stringResource(R.string.frecuencia_semanal)} · ${
            diasSemana.getOrElse(item.diaReferencia) { "" }
        }"
        "quincenal" -> {
            val segundo = if (item.diaReferencia + 15 <= 31) item.diaReferencia + 15
            else (item.diaReferencia + 15) - 31
            "${stringResource(R.string.frecuencia_quincenal)} · ${item.diaReferencia} y $segundo"
        }
        else -> "${stringResource(R.string.frecuencia_mensual)} · ${
            stringResource(R.string.dia_numero, item.diaReferencia)
        }"
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text(stringResource(R.string.eliminar_recurrente)) },
            text = { Text(stringResource(R.string.confirmar_eliminar_recurrente, item.nombre)) },
            confirmButton = {
                TextButton(onClick = {
                    onEliminar(item)
                    showDeleteDialog = false
                }) {
                    Text(stringResource(R.string.eliminar), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            },
            shape = RoundedCornerShape(16.dp)
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            // ── FILA PRINCIPAL ──
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Ícono tipo
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(containerColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp).rotate(45f),
                        tint = iconColor
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info central
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.nombre,
                        fontWeight = FontWeight.SemiBold,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(3.dp))

                    // Categoría • Frecuencia+día
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = with(CategoryTranslator) { context.traducirCategoria(item.categoria) },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Text(
                            text = " · ",
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                        Text(
                            text = textoFrecuencia,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                        )
                    }

                    Spacer(modifier = Modifier.height(3.dp))

                    // Método de pago
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CreditCard,
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = with(PaymentMethodTranslator) {
                                context.traducirMetodoPago(item.metodoPago)
                            },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                        )
                    }

                    // ✅ Advertencia día en meses cortos
                    if (item.frecuencia == "mensual" && item.diaReferencia > 28) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Info,
                                contentDescription = null,
                                modifier = Modifier.size(10.dp),
                                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = stringResource(R.string.dia_ajustado_mes_corto),
                                style = MaterialTheme.typography.bodySmall,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
                            )
                        }
                    }
                }

                // Cantidad + Switch
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = "${if (esIngreso) "+" else "-"}$${item.cantidad.formatMonto()}",
                        fontWeight = FontWeight.Bold,
                        color = iconColor,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Switch(
                        checked = item.activo,
                        onCheckedChange = { onToggle(item) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }
            }

            // ── BOTONES EDITAR / ELIMINAR ──
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = { onEditar(item) },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.editar), fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(stringResource(R.string.eliminar), fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun Recurrentes(
    onBotonCrear: () -> Unit,
    viewModel: RecurringViewModel = hiltViewModel()
) {
    val ingresos by viewModel.ingresos.collectAsState()
    val gastos by viewModel.gastos.collectAsState()


    var itemAEditar by remember { mutableStateOf<RecurringEntity?>(null) }

    if (itemAEditar != null) {
        EditarRecurrente(
            item = itemAEditar!!,
            onGuardar = { actualizado ->
                viewModel.actualizar(actualizado)
                itemAEditar = null
            },
            onVolver = { itemAEditar = null }
        )
        return
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onBotonCrear,
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding())
        ) {
            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(4.dp).height(20.dp)
                        .background(Color(0xFF2ECC71), RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.ingresos_recurrentes),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            if (ingresos.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_hay_ingresos_recurrentes),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(ingresos, key = { it.firestoreId }) { item ->
                    ItemRecurrente(
                        item = item,
                        onToggle = { viewModel.toggleActivo(it) },
                        onEditar = { itemAEditar = it },
                        onEliminar = { viewModel.eliminar(it) }
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.width(4.dp).height(20.dp)
                        .background(Color(0xFFE74C3C), RoundedCornerShape(2.dp)))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.gastos_recurrentes),
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            if (gastos.isEmpty()) {
                item {
                    Text(
                        text = stringResource(R.string.no_hay_gastos_recurrentes),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                items(gastos, key = { it.firestoreId }) { item ->
                    ItemRecurrente(
                        item = item,
                        onToggle = { viewModel.toggleActivo(it) },
                        onEditar = { itemAEditar = it },       // ✅
                        onEliminar = { viewModel.eliminar(it) } // ✅
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun EditarRecurrente(
    item: RecurringEntity,
    onGuardar: (RecurringEntity) -> Unit,
    onVolver: () -> Unit,
    paymentViewModel: PaymentMethodViewModel = hiltViewModel(),
    categoriasViewModel: CategoriasViewModel = hiltViewModel()
) {
    val metodosPago by paymentViewModel.metodosPago.collectAsState()
    val ingresos by categoriasViewModel.ingresos.collectAsState()
    val gastos by categoriasViewModel.gastos.collectAsState()
    val context = LocalContext.current
    var diaReferencia by remember { mutableStateOf(item.diaReferencia) }
    var nombre by remember { mutableStateOf(item.nombre) }
    var cantidad by remember { mutableStateOf(item.cantidad.toString()) }
    var categoria by remember { mutableStateOf(item.categoria) }
    var frecuencia by remember { mutableStateOf(item.frecuencia) }
    var metodoPago by remember { mutableStateOf(item.metodoPago) }
    var tipo by remember { mutableStateOf(item.tipo) }
    var expandidoCategoria by remember { mutableStateOf(false) }
    var expandidoMetodoPago by remember { mutableStateOf(false) }
    var nombreError by remember { mutableStateOf(false) }
    var cantidadError by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onVolver) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                }
                Text(
                    text = stringResource(R.string.editar_recurrente),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 22.sp
                )
            }
        }

        // Tipo
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(50.dp))
                    .padding(4.dp)
            ) {
                listOf("ingreso", "gasto").forEach { opcion ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(50.dp))
                            .background(when {
                                opcion == "ingreso" && tipo == "ingreso" -> Color(0xFF2ECC71)
                                opcion == "gasto" && tipo == "gasto" -> Color(0xFFE74C3C)
                                else -> Color.Transparent
                            })
                            .clickable { tipo = opcion }
                            .padding(vertical = 10.dp)
                    ) {
                        Text(
                            text = if (opcion == "ingreso") stringResource(R.string.ingreso)
                            else stringResource(R.string.gasto),
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
                onValueChange = { nombre = it; nombreError = false },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                isError = nombreError,
                supportingText = {
                    AnimatedVisibility(visible = nombreError) {
                        Text(stringResource(R.string.nombre_requerido),
                            color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
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
                onValueChange = { cantidad = it; cantidadError = false },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = cantidadError,
                supportingText = {
                    AnimatedVisibility(visible = cantidadError) {
                        Text(stringResource(R.string.cantidad_requerida),
                            color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
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
                    value = with(CategoryTranslator) { context.traducirCategoria(categoria) },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoCategoria) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandidoCategoria,
                    onDismissRequest = { expandidoCategoria = false }
                ) {
                    categoriasFiltradas.forEach { cat ->
                        DropdownMenuItem(
                            text = { Text(cat.getNombreTraducido()) },
                            onClick = {
                                categoria = cat.nombre
                                expandidoCategoria = false
                            }
                        )
                    }
                }
            }
        }

        // Frecuencia
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.frecuencia), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            val opciones = listOf(
                "diario" to stringResource(R.string.frecuencia_diario),
                "semanal" to stringResource(R.string.frecuencia_semanal),
                "quincenal" to stringResource(R.string.frecuencia_quincenal),
                "mensual" to stringResource(R.string.frecuencia_mensual)
            )
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
                                Text(
                                    text = label,
                                    fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal,
                                    color = if (seleccionado) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                    fontSize = 14.sp
                                )
                            }
                        }
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

        // Metodo de pago
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(stringResource(R.string.m_todo_de_pago), style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = expandidoMetodoPago,
                onExpandedChange = { expandidoMetodoPago = !expandidoMetodoPago }
            ) {
                OutlinedTextField(
                    value = with(PaymentMethodTranslator) { context.traducirMetodoPago(metodoPago) },
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandidoMetodoPago) },
                    modifier = Modifier.fillMaxWidth().menuAnchor(),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true
                )
                ExposedDropdownMenu(
                    expanded = expandidoMetodoPago,
                    onDismissRequest = { expandidoMetodoPago = false }
                ) {
                    metodosPago.forEach { metodo ->
                        DropdownMenuItem(
                            text = { Text(metodo.getNombreTraducido()) },
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
                onCancelChange = onVolver,
                onCreateChange = {
                    nombreError = nombre.isBlank()
                    cantidadError = cantidad.isBlank() || cantidad.toDoubleOrNull() == null

                    if (!nombreError && !cantidadError) {
                        onGuardar(item.copy(
                            nombre = nombre,
                            cantidad = cantidad.toDoubleOrNull() ?: 0.0,
                            categoria = categoria,
                            frecuencia = frecuencia,
                            diaReferencia = diaReferencia,
                            metodoPago = metodoPago,
                            tipo = tipo
                        ))
                        true
                    } else {
                        false
                    }
                }
            )
        }
    }
}