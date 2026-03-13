package com.example.clearcounts.ui.screens.IngresosGastos

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.data.repository.gasto.ExpenseRepository
import com.example.clearcounts.data.database.entities.ExpenseEntity
import com.example.clearcounts.data.database.entities.IncomeEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import com.example.clearcounts.R
import com.example.clearcounts.utils.CategoryTranslator
import com.example.clearcounts.utils.CategoryTranslator.traducirCategoria

const val MAX_LENGHT_OF_AMOUNT = 10
const val MAX_LENGHT_OF_NOTE = 200


@Composable
fun ingresos(
    ruta: String,
    icono: String,
    nombre: String,
    botonVolver:() -> Unit,
    viewModel: IngresosGastosViewModel = hiltViewModel(),
    botonCrearNavegacion:() -> Unit
){

    val formatoFecha: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    val formatoHora: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.US)

    var fechaSeleccionadaState by remember { mutableStateOf(LocalDate.now().format(formatoFecha)) }

    var horaSeleccionadaState by remember { mutableStateOf(LocalDateTime.now().format(formatoHora)) }

    val updateDate = { date: LocalDate ->
        fechaSeleccionadaState = date.format(formatoFecha)
    }

    var showTimePicker by remember { mutableStateOf(false) }

    var showTimeInputPicker by remember { mutableStateOf(false) }

    var showDatePicker by remember { mutableStateOf(false) }

    var nota by remember {mutableStateOf("")}

    var cantidad by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    val context = LocalContext.current
    val iconoId = remember(icono) {
        context.resources.getIdentifier(icono, "drawable", context.packageName)
    }

    val nombreTraducido = context.traducirCategoria(nombre)

    val rutaTraducida = when (ruta) {
        "ingreso" -> stringResource(R.string.ingreso)
        "gasto"   -> stringResource(R.string.gasto)
        else      -> ruta
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    focusManager.clearFocus()
                })
            }
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Row() {

                IconButton(
                    onClick = {
                        botonVolver()
                    },
                    modifier = Modifier
                        .padding(top = 10.dp, start = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.volver),
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 22.dp, start = 10.dp),
                    text = stringResource(R.string.realiza_un, rutaTraducida),
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(start = 10.dp, end = 10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp)
            ) {

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = if (iconoId != 0) painterResource(iconoId) else painterResource(R.drawable.camion),
                        contentDescription = nombre,
                        modifier = Modifier
                            .size(70.dp)
                            .padding(5.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = nombreTraducido,
                        fontSize = 20.sp
                    )
                }

            }

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            ){

            }

            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                CampoCantidad(
                    cantidad = cantidad,
                    onCantidadChange = { nuevoValor ->
                        if (nuevoValor.length <= MAX_LENGHT_OF_AMOUNT){
                            cantidad = nuevoValor
                        }},
                    modifier = Modifier.weight(1.3f)
                )

                CampoHora(
                    horaSeleccionada = horaSeleccionadaState,
                    onHoraSelecccionadaChange = {
                        showTimePicker = true
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                CampoFecha(
                    fechaSeleccionada = fechaSeleccionadaState,
                    onDateClick = {
                        showDatePicker = true
                    },
                    modifier = Modifier.weight(1f),
                    label = stringResource(R.string.fecha_ingresosGastos)
                )

                CampoDiasDesplegable(
                    onDiaSelected = { dia ->
                        when (dia) {
                            DiaOpcion.AYER   -> updateDate(LocalDate.now().minusDays(1))
                            DiaOpcion.HOY    -> updateDate(LocalDate.now())
                            DiaOpcion.MANANA -> updateDate(LocalDate.now().plusDays(1))
                        }
                    },
                    modifier = Modifier.weight(1.1f)
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

            Spacer(modifier = Modifier.height(100.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            Column {
                BotonesInferiores(
                    onCancelChange = botonVolver,
                    onCreateChange = {

                        if (cantidad.isNotBlank() && cantidad.toDoubleOrNull() != null) {

                            val onSuccess = { botonCrearNavegacion() }

                            if (ruta == "ingreso") {
                                val newIncome = IncomeEntity(
                                    id = 0,
                                    categoria = nombre,
                                    cantidad = cantidad.toDouble(),
                                    hora = horaSeleccionadaState,
                                    fecha = fechaSeleccionadaState,
                                    nota = nota
                                )
                                viewModel.insertIncome(newIncome, onSuccess)
                            } else {
                                val newExpense = ExpenseEntity(
                                    id = 0,
                                    categoria = nombre,
                                    cantidad = cantidad.toDouble(),
                                    hora = horaSeleccionadaState,
                                    fecha = fechaSeleccionadaState,
                                    nota = nota
                                )
                                viewModel.insertExpense(newExpense, onSuccess)
                            }
                        }

                    }
                )
            }

        }

    }

    if (showTimePicker) {
        TimePickerDialog(
            onDismiss = {
                showTimePicker = false
            },
            onConfirm = { selectedTime ->

                val newTime = selectedTime.format(formatoHora)

                horaSeleccionadaState = newTime

                showTimePicker = false
            },
            onIconChange = {

                showTimePicker = false

                showTimeInputPicker = true
            }
        )
    }

    if (showTimeInputPicker) {
        TimeInputDialog(
            onDismiss = {
                showTimeInputPicker = false
            },
            onConfirm = {selectedTime ->

                val newTime = selectedTime.format(formatoHora)

                horaSeleccionadaState = newTime

                showTimePicker = false
            },
            onIconChange = {
                showTimePicker = true
            }
        )
    }

    if (showDatePicker){
        DatePickerDialogComposable(
            onConfirm = { selectedDate ->

                updateDate(selectedDate)

                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }
}

@Composable
fun CampoFecha(
    fechaSeleccionada: String,
    onDateClick:() -> Unit,
    modifier: Modifier = Modifier,
    label: String
){

    OutlinedTextField(
        value = fechaSeleccionada,
        onValueChange = {},
        readOnly = true,
        shape = RoundedCornerShape(20.dp),
        label = { Text(label) },
        modifier = modifier,
        trailingIcon = {

            Icon(
                Icons.Default.DateRange,
                contentDescription = "Seleccionar fecha",
                modifier = Modifier.clickable(
                    onClick = onDateClick
                )
            )
        }
    )
}

@Composable
fun CampoNota(
    nota: String,
    onNotaChange: (String) -> Unit
){

    OutlinedTextField(
        value = nota,
        onValueChange = onNotaChange,
        minLines = 3,
        maxLines = 5,
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.nota_ingresos_gastos)) },
        placeholder = { Text(stringResource(R.string.escribe_una_nota))},
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    )
}

@Composable
fun CampoCantidad(
    cantidad: String,
    onCantidadChange:(String) -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = cantidad,
        singleLine = true,
        onValueChange = onCantidadChange,
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.cantidad)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        trailingIcon = {
            Icon(Icons.Default.Calculate, contentDescription = stringResource(R.string.escribir_cantidad))
        }
    )
}

@Composable
fun CampoHora(
    horaSeleccionada: String,
    onHoraSelecccionadaChange: () -> Unit,
    modifier: Modifier = Modifier
){
    OutlinedTextField(
        value = horaSeleccionada,
        onValueChange = {},
        readOnly = true,
        shape = RoundedCornerShape(20.dp),
        label = { Text(stringResource(R.string.hora_ingresos_gastos)) },
        modifier = modifier,
        trailingIcon = {

            Icon(Icons.Default.Schedule, contentDescription = stringResource(R.string.seleccionar_hora),
                modifier = Modifier.clickable(
                    onClick = onHoraSelecccionadaChange
                ))
        }
    )
}

enum class DiaOpcion {
    AYER, HOY, MANANA
}
@Composable
fun CampoDiasDesplegable(
    onDiaSelected: (DiaOpcion) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val options = listOf(
        DiaOpcion.AYER  to stringResource(R.string.ayer),
        DiaOpcion.HOY   to stringResource(R.string.hoy),
        DiaOpcion.MANANA to stringResource(R.string.mañana)
    )

    var diaSeleccionado by remember { mutableStateOf(DiaOpcion.HOY) }

    val mainButtonText = options.first { it.first == diaSeleccionado }.second

    Box(
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp)
        ) {
            Button(
                onClick = {
                    if (diaSeleccionado != DiaOpcion.HOY) {
                        diaSeleccionado = DiaOpcion.HOY
                        onDiaSelected(DiaOpcion.HOY)
                    }
                },
                shape = MaterialTheme.shapes.medium.copy(topEnd = ZeroCornerSize, bottomEnd = ZeroCornerSize),
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = mainButtonText,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Button(
                modifier = Modifier.height(50.dp),
                onClick = { expanded = true },
                shape = MaterialTheme.shapes.medium.copy(topStart = ZeroCornerSize, bottomStart = ZeroCornerSize),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { (dia, label) ->
                    DropdownMenuItem(
                        text = { Text(label) },
                        onClick = {
                            diaSeleccionado = dia
                            onDiaSelected(dia)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BotonesInferiores(
    onCancelChange:() -> Unit,
    onCreateChange:() -> Unit
){

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        onClick = {
            onCreateChange()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Text(
            stringResource(R.string.crear),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontSize = 15.sp
        )
    }

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        onClick = onCancelChange
    ){
        Text(stringResource(R.string.cancelar),
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 15.sp
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("NewApi")
@Composable
fun TimePickerDialog(
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    onIconChange: () -> Unit,
){
    val currentTime = LocalDateTime.now()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = false
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface (
            color = MaterialTheme.colorScheme.surface
        ){
            Column {

                TimePicker(state = timePickerState, modifier = Modifier.padding(10.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Keyboard,
                        contentDescription = "Hora escrita",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable(
                                onClick = onIconChange
                            )
                    )

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){


                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(stringResource(R.string.cancelar))
                        }

                        TextButton(onClick = {
                            val selectedTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onConfirm(selectedTime)
                            onDismiss()
                        }) {
                            Text(stringResource(R.string.aceptar))
                        }
                    }

                }
            }
        }
    }
}

@SuppressLint("NewApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeInputDialog(
    onConfirm: (LocalTime) -> Unit,
    onDismiss: () -> Unit,
    onIconChange: () -> Unit
){

    val currentTime = LocalDateTime.now()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = false
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface (
            color = MaterialTheme.colorScheme.surface
        ){
            Column {

                TimeInput(state = timePickerState, modifier = Modifier.padding(10.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = "Hora escrita",
                        modifier = Modifier
                            .padding(start = 10.dp)
                            .clickable(
                                onClick = onIconChange
                            )
                    )

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){


                        TextButton(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.background,
                                contentColor = MaterialTheme.colorScheme.onBackground
                            )
                        ) {
                            Text(stringResource(R.string.cancelar))
                        }

                        TextButton(onClick = {
                            val selectedTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onConfirm(selectedTime)
                            onDismiss()
                        }) {
                            Text(stringResource(R.string.aceptar))
                        }
                    }

                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun DatePickerDialogComposable(
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Instant.now().toEpochMilli()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // ... (Lógica de conversión y onConfirm) ...
                val selectedMillis = datePickerState.selectedDateMillis
                if (selectedMillis != null) {
                    val selectedDate = Instant.ofEpochMilli(selectedMillis)
                        .atZone(ZoneId.of("UTC"))
                        .toLocalDate()
                    onConfirm(selectedDate)
                }
                onDismiss()
            }) {
                Text(stringResource(R.string.aceptar))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancelar))
            }
        }
    ) {
        // El DatePicker se ajustará automáticamente dentro del diálogo de Material 3
        DatePicker(state = datePickerState)
    }
}