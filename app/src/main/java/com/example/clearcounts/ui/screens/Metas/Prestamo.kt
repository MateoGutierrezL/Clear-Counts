package com.example.clearcounts.ui.screens.Metas

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.material3.Text
import com.example.clearcounts.data.database.entities.BudgetEntity
import com.example.clearcounts.ui.screens.IngresosGastos.BotonesInferiores
import com.example.clearcounts.ui.screens.IngresosGastos.CampoFecha
import com.example.clearcounts.ui.screens.IngresosGastos.CampoNota
import com.example.clearcounts.ui.screens.IngresosGastos.DatePickerDialogComposable
import com.example.clearcounts.ui.screens.IngresosGastos.MAX_LENGHT_OF_AMOUNT
import com.example.clearcounts.ui.screens.IngresosGastos.MAX_LENGHT_OF_NOTE
import java.time.LocalDate
import java.time.format.DateTimeFormatter

val MAX_NOMBRE_LENGTH = 20
val MAX_NOMBREPRESTADOR_LENGTH = 40

@Composable
fun Prestamo(
    botonVolver:() -> Unit,
    titulo: String,
    icono: ImageVector,
    budgetAEditar: BudgetEntity? = null,
    viewModel: MetasViewModel = hiltViewModel()
){

    var nombreMovimiento by remember { mutableStateOf(budgetAEditar?.nombre ?: "") }
    var cantidadRequerida by remember { mutableStateOf(budgetAEditar?.cantidadRequerida?.toString() ?: "") }
    var cantidadAcumulada by remember { mutableStateOf(budgetAEditar?.cantidadAcumulada?.toString() ?: "") }
    var prestador by remember { mutableStateOf(budgetAEditar?.prestador ?: "") }
    var nota by remember { mutableStateOf(budgetAEditar?.nota ?: "") }

    val formatoFecha: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    var fechaInicioSeleccionadaState by remember { mutableStateOf(budgetAEditar?.fechaInicio ?: LocalDate.now().format(formatoFecha)) }
    var fechaLimiteSeleccionadaState by remember { mutableStateOf(budgetAEditar?.fechaLimite ?: LocalDate.now().format(formatoFecha)) }

    var tipoFechaEdicion by remember { mutableStateOf<String?>(null) }

    val updateInicioDate = { date: LocalDate ->
        fechaInicioSeleccionadaState = date.format(formatoFecha)
    }

    val updateLimiteDate = { date: LocalDate ->
        fechaLimiteSeleccionadaState = date.format(formatoFecha)
    }

    LaunchedEffect(budgetAEditar) {
        budgetAEditar?.let {
            nombreMovimiento = it.nombre
            cantidadRequerida = it.cantidadRequerida.toBigDecimal().toPlainString()
            cantidadAcumulada = it.cantidadAcumulada.toBigDecimal().toPlainString()
            prestador = it.prestador ?: ""
            nota = it.nota ?: ""
            fechaInicioSeleccionadaState = it.fechaInicio ?: LocalDate.now().format(formatoFecha)
            fechaLimiteSeleccionadaState = it.fechaLimite ?: LocalDate.now().format(formatoFecha)
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp)
    ) {
        
        item {
            Row(
                modifier = Modifier.fillMaxWidth()
            ){

                IconButton(
                    onClick = {
                        botonVolver()
                    },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }

                TituloPantalla(
                    titulo = titulo
                )

            }

        }

        item {
            CardEncabezadoSimple(
                nombre = nombreMovimiento,
                onNombreChange = { nuevoTexto ->

                    if (nuevoTexto.length <= MAX_NOMBRE_LENGTH){
                        nombreMovimiento = nuevoTexto
                    }
                },
                icono = icono
            )
        }

        item {
            Montos(
                cantidadRequerida = cantidadRequerida,
                cantidadAcumulada = cantidadAcumulada,
                onCantidadAcumuladaChange = { nuevoTexto ->

                    if(nuevoTexto.length <= MAX_LENGHT_OF_AMOUNT){
                        cantidadAcumulada = nuevoTexto
                    }
                },
                onCantidadRequeridaChange = { nuevoTexto ->

                    if(nuevoTexto.length <= MAX_LENGHT_OF_AMOUNT){
                        cantidadRequerida = nuevoTexto
                    }
                }
            )
        }

        item {
            Prestador(
                label = "Nombre del prestador",
                value = prestador,
                onValueChange = { nuevoTexto ->

                    if (nuevoTexto.length <= MAX_NOMBREPRESTADOR_LENGTH){
                        prestador = nuevoTexto
                    }
                }
            )
        }

        item {

            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Fechas",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF2196F3),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    HorizontalDivider(
                        modifier = Modifier.weight(1f),
                        thickness = 1.dp,
                        color = Color(0xFFE0E0E0)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    CampoFecha(
                        fechaSeleccionada = fechaInicioSeleccionadaState,
                        onDateClick = {
                            tipoFechaEdicion = "inicio"
                        },
                        modifier = Modifier.weight(1f),
                        label = "Fecha inicio"
                    )

                    CampoFecha(
                        fechaSeleccionada = fechaLimiteSeleccionadaState,
                        onDateClick = {
                            tipoFechaEdicion = "limite"
                        },
                        modifier = Modifier.weight(1f),
                        label = "Fecha límite"
                    )
                }
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(
                    text = "Nota",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color(0xFFE0E0E0)
                )
            }

            CampoNota(
                nota = nota,
                onNotaChange = { nuevoValor ->

                    if (nuevoValor.length <= MAX_LENGHT_OF_NOTE){
                        nota = nuevoValor
                    }
                }
            )
        }

        item {

            Spacer(modifier = Modifier.height(40.dp))

            BotonesInferiores(
                onCancelChange = {
                    botonVolver()
                },
                onCreateChange = {
                    if (nombreMovimiento.isNotBlank() && cantidadRequerida.isNotBlank()) {
                        if (budgetAEditar != null) {
                            // Actualizar
                            viewModel.actualizarBudget(
                                budgetAEditar.copy(
                                    nombre = nombreMovimiento,
                                    cantidadRequerida = cantidadRequerida.toDoubleOrNull() ?: 0.0,
                                    cantidadAcumulada = cantidadAcumulada.toDoubleOrNull() ?: 0.0,
                                    prestador = prestador,
                                    fechaInicio = fechaInicioSeleccionadaState,
                                    fechaLimite = fechaLimiteSeleccionadaState,
                                    nota = nota
                                )
                            )
                        } else {
                            // Crear nuevo
                            viewModel.guardarPrestamo(
                                nombre = nombreMovimiento,
                                cantidadRequerida = cantidadRequerida,
                                cantidadAcumulada = cantidadAcumulada,
                                prestador = prestador,
                                fechaInicio = fechaInicioSeleccionadaState,
                                fechaLimite = fechaLimiteSeleccionadaState,
                                nota = nota,
                                tipo = titulo
                            )
                        }
                        botonVolver()
                    }
                }
            )
        }
    }

    if (tipoFechaEdicion != null) {
        DatePickerDialogComposable(
            onConfirm = { selectedDate ->

                when (tipoFechaEdicion) {
                    "inicio" -> updateInicioDate(selectedDate)
                    "limite" -> updateLimiteDate(selectedDate)
                }
                tipoFechaEdicion = null
            },
            onDismiss = {
                tipoFechaEdicion = null
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewPrestamo(){
    Prestamo(
        botonVolver = {

        },
        titulo = "Meta",
        icono = Icons.Default.Person
    )
}

@Composable
fun TituloPantalla(
    titulo: String
){
    Text(
        text = titulo,
        modifier = Modifier.padding(vertical = 22.dp, horizontal = 16.dp),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
fun Montos(
    cantidadRequerida: String,
    cantidadAcumulada: String,
    onCantidadRequeridaChange: (String) -> Unit,
    onCantidadAcumuladaChange: (String) -> Unit
) {

    Column(modifier = Modifier.padding(16.dp)) {
        // --- Encabezado con Línea ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Montos",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color(0xFFE0E0E0)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MontoInput(
                label = "Cantidad requerida",
                value = cantidadRequerida,
                onValueChange = onCantidadRequeridaChange,
                modifier = Modifier.weight(1f)
            )
            MontoInput(
                label = "Cantidad acumulada",
                value = cantidadAcumulada,
                onValueChange = onCantidadAcumuladaChange,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun MontoInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Cantidad", color = MaterialTheme.colorScheme.onBackground)},
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.onBackground,
                focusedBorderColor = Color(0xFF2196F3)
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
}

@Composable
fun CardEncabezadoSimple(
    nombre: String,
    onNombreChange: (String) -> Unit,
    icono: ImageVector
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono de Usuario (Decorativo)
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainerLow, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = Color(0xFF2196F3)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Campo de texto con el icono de edición como decoración
                OutlinedTextField(
                    value = nombre,
                    onValueChange = onNombreChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ej. Préstamo personal", color = MaterialTheme.colorScheme.onBackground) },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null, // Solo decoración
                            tint = Color(0xFF2196F3),
                            modifier = Modifier.size(20.dp)
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold
                    )
                )

                Text(
                    text = "Toca para editar el nombre",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 12.dp, top = 2.dp)
                )
            }
        }
    }
}

@Composable
fun Prestador(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange:(String) -> Unit
){
    Column(modifier = Modifier.padding(16.dp)) {
        // --- Encabezado con Línea ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Prestador",
                style = MaterialTheme.typography.labelLarge,
                color = Color(0xFF2196F3),
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                thickness = 1.dp,
                color = Color(0xFFE0E0E0)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = modifier) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = {Text("El: Banco, Persona, Entidad... ", color = MaterialTheme.colorScheme.onBackground)},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )
        }
    }
}