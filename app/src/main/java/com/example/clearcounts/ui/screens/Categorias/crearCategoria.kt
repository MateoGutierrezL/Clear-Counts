package com.example.clearcounts.ui.screens.Categorias

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.IngresosGastos.BotonesInferiores


@Composable
fun crearCategoria(
    tipo: String,
    botonVolver: () -> Unit,
    viewModel: CategoriasViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var nombre by remember { mutableStateOf("") }
    var iconoSeleccionado by remember { mutableStateOf("") }
    var tipoSeleccionado by remember { mutableStateOf(tipo) }

    // Lista de iconos disponibles en tu proyecto
    val iconosDisponibles = listOf(
        "comida", "trasnporte", "salud", "deporte", "educacion",
        "ropa", "alquiler", "libros", "maquillaje", "plan_datos",
        "salario", "comision", "inversiones", "regalo", "reembolso",
        "gasolina", "camion", "corazon", "administracion", "alerta"

    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            }
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {

            // Header
            Row {
                IconButton(
                    onClick = botonVolver,
                    modifier = Modifier.padding(top = 10.dp, start = 5.dp)
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
                    text = stringResource(R.string.nueva_categoria),
                    fontSize = 22.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Preview de la categoria
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val iconoId = remember(iconoSeleccionado) {
                        if (iconoSeleccionado.isNotEmpty())
                            context.resources.getIdentifier(iconoSeleccionado, "drawable", context.packageName)
                        else 0
                    }
                    if (iconoId != 0) {
                        Image(
                            painter = painterResource(iconoId),
                            contentDescription = nombre,
                            modifier = Modifier
                                .size(70.dp)
                                .padding(5.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(70.dp)
                                .padding(5.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Sin icono",
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (nombre.isNotEmpty()) nombre else stringResource(R.string.nombre_de_categoria),
                        fontSize = 20.sp,
                        color = if (nombre.isNotEmpty()) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo nombre
            OutlinedTextField(
                value = nombre,
                singleLine = true,
                onValueChange = { if (it.length <= 20) nombre = it },
                label = { Text(stringResource(R.string.nombre)) },
                placeholder = { Text(stringResource(R.string.ej_mascota)) },
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selector tipo
            Text(
                text = stringResource(R.string.tipo_de_categoria),
                modifier = Modifier.padding(start = 14.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { tipoSeleccionado = "ingreso" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tipoSeleccionado == "ingreso")
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) { Text(stringResource(R.string.ingreso), color = MaterialTheme.colorScheme.onPrimaryContainer) }

                Button(
                    onClick = { tipoSeleccionado = "gasto" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (tipoSeleccionado == "gasto")
                            MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.secondaryContainer
                    )
                ) { Text(stringResource(R.string.gasto), color = MaterialTheme.colorScheme.onPrimaryContainer) }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selector de iconos
            Text(
                text = stringResource(R.string.selecciona_un_icono),
                modifier = Modifier.padding(start = 14.dp),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(iconosDisponibles) { icono ->
                    val iconoId = context.resources.getIdentifier(icono, "drawable", context.packageName)
                    if (iconoId != 0) {
                        Box(
                            modifier = Modifier
                                .size(55.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (iconoSeleccionado == icono)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else MaterialTheme.colorScheme.secondaryContainer
                                )
                                .clickable { iconoSeleccionado = icono },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(iconoId),
                                contentDescription = icono,
                                modifier = Modifier
                                    .size(38.dp)
                                    .padding(4.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // Botones inferiores
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                BotonesInferiores(
                    onCancelChange = botonVolver,
                    onCreateChange = {
                        if (nombre.isNotBlank() && iconoSeleccionado.isNotEmpty()) {
                            viewModel.insertCategoria(nombre, iconoSeleccionado, tipoSeleccionado)
                            botonVolver()
                        }
                    }
                )
            }
        }
    }
}
