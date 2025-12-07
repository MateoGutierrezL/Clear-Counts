package com.example.clearcounts.ui.screens.Categorias

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.Paint
import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
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
import androidx.compose.material3.TimePickerState
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.InicioSesion.RegisterSucces
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@SuppressLint("NewApi")
@Composable
fun ingresos(
    icono: Int,
    nombre: String,
    botonVolver:() -> Unit
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
                        contentDescription = "Volver",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 22.dp, start = 10.dp),
                    text = "Realiza un ingreso",
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(start = 10.dp, end = 10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp)
            ) {

                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        painter = painterResource(icono),
                        contentDescription = nombre,
                        modifier = Modifier.size(70.dp).padding(5.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = nombre,
                        fontSize = 20.sp
                    )
                }

            }

            Row (
                modifier = Modifier.fillMaxWidth().padding(start = 10.dp)
            ){

            }

            Spacer(modifier = Modifier.height(10.dp))

            Row (
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                CampoCantidad(
                    cantidad = cantidad,
                    onCantidadChange = { cantidad = it },
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
                    modifier = Modifier.weight(1f)
                )

                CampoDiasDesplegable(
                    onDiaSelected = { option ->
                        when (option) {
                            "Ayer" -> updateDate(LocalDate.now().minusDays(1))
                            "Mañana" -> updateDate(LocalDate.now().plusDays(1))
                            "Hoy" -> updateDate(LocalDate.now())
                        }
                    },
                    modifier = Modifier.weight(1.1f)
                )
            }

            CampoNota(
                nota = nota,
                onNotaChange = {nota = it}
            )

            Spacer(modifier = Modifier.height(100.dp))
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ){
            Column {
                BotonesInferiores()
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
        DatePickerDialog(
            onConfirm = { selectedDate ->

                updateDate(selectedDate)

                showDatePicker = false
            },
            onDismiss = {
                showDatePicker = false
            },
            onIconChange = {}
        )
    }
}

@Composable
fun CampoFecha(
    fechaSeleccionada: String,
    onDateClick:() -> Unit,
    modifier: Modifier = Modifier
){

    OutlinedTextField(
        value = fechaSeleccionada,
        onValueChange = {},
        readOnly = true,
        shape = RoundedCornerShape(20.dp),
        label = { Text("Fecha") },
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
        maxLines = 5,
        shape = RoundedCornerShape(20.dp),
        label = { Text("Nota") },
        placeholder = { Text("Escribe una nota")},
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp)
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
        label = { Text("Cantidad") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = modifier,
        trailingIcon = {
            Icon(Icons.Default.Calculate, contentDescription = "Escribir cantidad")
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
        label = { Text("Hora") },
        modifier = modifier,
        trailingIcon = {

            Icon(Icons.Default.Schedule, contentDescription = "Seleccionar Hora",
                modifier = Modifier.clickable(
                    onClick = onHoraSelecccionadaChange
                ))
        }
    )
}

@Composable
fun CampoDiasDesplegable(
    onDiaSelected:(String) -> Unit,
    modifier: Modifier = Modifier
){

    var expanded by remember { mutableStateOf(false) }

    val options = listOf("Ayer", "Hoy", "Mañana")

    var mainButtonText by remember { mutableStateOf(options[1]) }

    Box(
        modifier = modifier.wrapContentSize(Alignment.TopStart)
    ){
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        ) {
            Button(
                onClick = {
                    if (mainButtonText != "Hoy") {
                        mainButtonText = "Hoy"
                        onDiaSelected("Hoy")
                    }
                },
                shape = MaterialTheme.shapes.medium.copy(topEnd = ZeroCornerSize, bottomEnd = ZeroCornerSize),
                modifier = Modifier.weight(1f).height(50.dp)
            ) {
                Text(mainButtonText)
            }

            Button(
                modifier = Modifier.height(50.dp),
                onClick = { expanded = true },
                shape = MaterialTheme.shapes.medium.copy(topStart = ZeroCornerSize, bottomStart = ZeroCornerSize),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(horizontal = 8.dp)
            ) {
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Desplegar opciones de fecha",
                    modifier = Modifier.size(24.dp)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            mainButtonText = option
                            onDiaSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}


@Composable
fun BotonesInferiores(){

    Button(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
        onClick = {},
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text("Crear",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 15.sp
        )
    }

    OutlinedButton(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
        onClick = {}
    ){
        Text("Cancelar",
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
                        modifier = Modifier.padding(start = 10.dp)
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
                            Text("Cancelar")
                        }

                        TextButton(onClick = {
                            val selectedTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onConfirm(selectedTime)
                            onDismiss()
                        }) {
                            Text("Aceptar")
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
                        modifier = Modifier.padding(start = 10.dp)
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
                            Text("Cancelar")
                        }

                        TextButton(onClick = {
                            val selectedTime = LocalTime.of(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onConfirm(selectedTime)
                            onDismiss()
                        }) {
                            Text("Aceptar")
                        }
                    }

                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun DatePickerDialog(
    onConfirm: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
    onIconChange: () -> Unit
){

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = java.time.Instant.now().toEpochMilli()
    )

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface (
            color = MaterialTheme.colorScheme.surface
        ){
            Column {

                DatePicker(state = datePickerState, modifier = Modifier.padding(10.dp))

                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    TextButton(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground
                        )
                    ) {
                        Text("Cancelar")
                    }

                    TextButton(onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null){

                            val selectedDate = java.time.Instant.ofEpochMilli(selectedMillis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()

                            onConfirm(selectedDate)
                        }
                        onDismiss()
                    }) {
                        Text("Aceptar")
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ingresosPreview(){
    ingresos(
        icono = R.drawable.ingresos,
        nombre = "Ingresos",
        botonVolver = {}
    )
}