package com.example.clearcounts.ui.screens.Barras

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.clearcounts.ui.navigation.Pantallas
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.Categorias.CategoriasItem
import com.example.clearcounts.ui.screens.Categorias.CategoriasViewModel
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.filled.Alarm
import com.example.clearcounts.utils.PaymentMethodTranslator.traducirMetodoPago


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomAppBar(
    selectedIcon: MutableState<String>, // Cambiado a String para identificar iconos
    navigationController: NavHostController,
    categoriasViewModel: CategoriasViewModel = hiltViewModel()
) {
    val context = LocalContext.current.applicationContext

    BottomAppBar(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) {
        // Home
        NavigationBarItem(
            selected = selectedIcon.value == "home",
            onClick = {
                selectedIcon.value = "home"
                navigationController.navigate(Pantallas.Inicio.pantalla) {
                    popUpTo(0)
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.home),
                    contentDescription = stringResource(R.string.inicio),
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "home") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.inicio),
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "home") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = Color.White,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = Color.Transparent
            )
        )

        // Gráficas
        NavigationBarItem(
            selected = selectedIcon.value == "charts",
            onClick = {
                selectedIcon.value = "charts"
                navigationController.navigate(Pantallas.Graficas.pantalla) {
                    popUpTo(0)
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.graph),
                    contentDescription = stringResource(R.string.graficas),
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "charts") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.graficas),
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "charts") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = Color.White,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = Color.Transparent
            )
        )

        val sheetState = rememberModalBottomSheetState()
        var isSheetOpen by rememberSaveable {
            mutableStateOf(false)
        }
        // Botón central con FAB personalizado
        NavigationBarItem(
            selected = false,
            onClick = {
                isSheetOpen = true
            },
            icon = {
                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onPrimaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(R.string.añadir),
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
        if (isSheetOpen) {
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = { isSheetOpen = false }
            ) {
                val categoriasViewModel: CategoriasViewModel = hiltViewModel()
                val paymentViewModel: PaymentMethodViewModel = hiltViewModel()

                var paso by rememberSaveable { mutableStateOf(1) } // 1 = métodos pago, 2 = categorías
                var metodoPagoSeleccionado by rememberSaveable { mutableStateOf("") }
                var ingreso by rememberSaveable { mutableStateOf(true) }
                var gasto by rememberSaveable { mutableStateOf(false) }

                val gastos by categoriasViewModel.gastos.collectAsState()
                val ingresos by categoriasViewModel.ingresos.collectAsState()
                val metodosPago by paymentViewModel.metodosPago.collectAsState()

                var showCrearMetodoDialog by rememberSaveable { mutableStateOf(false) }
                var nuevoMetodoNombre by rememberSaveable { mutableStateOf("") }

                val context = LocalContext.current
                val scope = rememberCoroutineScope()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surface)
                ) {
                    // Animación entre pasos
                    AnimatedContent(
                        targetState = paso,
                        transitionSpec = {
                            if (targetState > initialState) {
                                slideInHorizontally { it } + fadeIn() togetherWith
                                        slideOutHorizontally { -it } + fadeOut()
                            } else {
                                slideInHorizontally { -it } + fadeIn() togetherWith
                                        slideOutHorizontally { it } + fadeOut()
                            }
                        },
                        label = "modal_steps"
                    ) { currentPaso ->

                        if (currentPaso == 1) {
                            // ── PASO 1: MÉTODO DE PAGO
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                item {
                                    Text(
                                        text = stringResource(R.string.como_realizaste_movimiento),
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(vertical = 16.dp)
                                    )
                                }

                                items(metodosPago) { metodo ->
                                    MetodoPagoItem(
                                        metodo = metodo,
                                        context = context,
                                        onClick = {
                                            metodoPagoSeleccionado = metodo.nombre
                                            paso = 2
                                        },
                                        onDelete = if (!metodo.esDefault) {
                                            { paymentViewModel.deleteMetodoPago(metodo) }
                                        } else null
                                    )
                                }

                                item {
                                    // Botón agregar nuevo método
                                    OutlinedButton(
                                        onClick = { showCrearMetodoDialog = true },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp),
                                        shape = RoundedCornerShape(16.dp)
                                    ) {
                                        Icon(Icons.Default.Add, contentDescription = null)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(text = stringResource(R.string.agregar_metodo_pago))
                                    }
                                }
                            }

                        } else {
                            // ── PASO 2: CATEGORÍAS ──
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(MaterialTheme.colorScheme.surface)
                            ) {
                                item {
                                    // Header con botón volver
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(onClick = { paso = 1 }) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                                contentDescription = "Volver"
                                            )
                                        }
                                        Text(
                                            text = LocalContext.current.traducirMetodoPago(metodoPagoSeleccionado),
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    // Botones Ingreso/Gasto
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Button(
                                            onClick = { ingreso = true; gasto = false },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (ingreso) MaterialTheme.colorScheme.primaryContainer
                                                else MaterialTheme.colorScheme.secondaryContainer
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(R.string.ingresos),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        Button(
                                            onClick = { gasto = true; ingreso = false },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = if (gasto) MaterialTheme.colorScheme.primaryContainer
                                                else MaterialTheme.colorScheme.secondaryContainer
                                            )
                                        ) {
                                            Text(
                                                text = stringResource(R.string.gastos),
                                                color = MaterialTheme.colorScheme.onPrimaryContainer
                                            )
                                        }
                                        Button(
                                            modifier = Modifier.width(60.dp),
                                            onClick = {
                                                val tipo = if (ingreso) "ingreso" else "gasto"
                                                navigationController.navigate("crearCategoria/$tipo")
                                            },
                                            colors = ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                                            )
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = "Añadir",
                                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.size(16.dp))

                                    // Categorías filtradas
                                    if (ingreso) {
                                        ingresos.forEach { categoria ->
                                            CategoriasItem(
                                                navController = navigationController,
                                                categorias = categoria,
                                                sheetState = sheetState,
                                                ruta = "ingreso",
                                                metodoPago = metodoPagoSeleccionado // 👈
                                            )
                                        }
                                    } else {
                                        gastos.forEach { categoria ->
                                            CategoriasItem(
                                                navController = navigationController,
                                                categorias = categoria,
                                                sheetState = sheetState,
                                                ruta = "gasto",
                                                metodoPago = metodoPagoSeleccionado // 👈
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                val iconosDisponibles = listOf(
                    "efectivo", "debito", "credito", "billetera",
                    "banco", "nequi", "daviplata", "bolsillo"
                )

                var iconoSeleccionado by rememberSaveable { mutableStateOf("efectivo") }


                if (showCrearMetodoDialog) {
                    AlertDialog(
                        onDismissRequest = { showCrearMetodoDialog = false },
                        title = { Text(stringResource(R.string.nuevo_metodo_pago)) },
                        text = {
                            Column {
                                OutlinedTextField(
                                    value = nuevoMetodoNombre,
                                    onValueChange = { nuevoMetodoNombre = it },
                                    label = { Text("Nombre") },
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Text(
                                    text = stringResource(R.string.selecciona_un_icono),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Grilla de iconos
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(4),
                                    modifier = Modifier.height(160.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    items(iconosDisponibles) { icono ->
                                        val iconoId = context.resources.getIdentifier(
                                            icono, "drawable", context.packageName
                                        )
                                        val isSelected = icono == iconoSeleccionado

                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                                    else MaterialTheme.colorScheme.surfaceContainerHigh
                                                )
                                                .border(
                                                    width = if (isSelected) 2.dp else 0.dp,
                                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                                    else Color.Transparent,
                                                    shape = RoundedCornerShape(12.dp)
                                                )
                                                .clickable { iconoSeleccionado = icono },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (iconoId != 0) {
                                                Icon(
                                                    painter = painterResource(id = iconoId),
                                                    contentDescription = icono,
                                                    modifier = Modifier.size(32.dp),
                                                    tint = Color.Unspecified
                                                )
                                            } else {
                                                Text(
                                                    text = icono.first().uppercase(),
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 16.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                if (nuevoMetodoNombre.isNotBlank()) {
                                    paymentViewModel.insertMetodoPago(
                                        nombre = nuevoMetodoNombre,
                                        icono = iconoSeleccionado // 👈 Usa el icono seleccionado
                                    )
                                    nuevoMetodoNombre = ""
                                    iconoSeleccionado = "efectivo"
                                    showCrearMetodoDialog = false
                                }
                            }) { Text(stringResource(R.string.aceptar)) }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                showCrearMetodoDialog = false
                                nuevoMetodoNombre = ""
                                iconoSeleccionado = "efectivo"
                            }) { Text(stringResource(R.string.cancelar)) }
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                }


            }
        }

        /* */
        // Presupuesto
        NavigationBarItem(
            selected = selectedIcon.value == "budget",
            onClick = {
                selectedIcon.value = "budget"
                navigationController.navigate(Pantallas.Presupuesto.pantalla) {
                    popUpTo(0)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.TrackChanges,
                    contentDescription = stringResource(R.string.metas),
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "budget") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.metas),
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "budget") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = Color.White,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = Color.Transparent
            )
        )

        // Perfil
        NavigationBarItem(
            selected = selectedIcon.value == "recurrentes",
            onClick = {
                selectedIcon.value = "recurrentes"
                navigationController.navigate(Pantallas.Recurrentes.pantalla) {
                    popUpTo(0)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = stringResource(R.string.recurrentes),
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "recurrentes") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.recurrentes),
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "recurrentes") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                selectedTextColor = Color.White,
                unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                indicatorColor = Color.Transparent
            )
        )
    }
}



