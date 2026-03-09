package com.example.clearcounts.ui.screens.Barras

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.TrackChanges
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.Categorias.CategoriasItem
import com.example.clearcounts.ui.screens.Categorias.CategoriasViewModel



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
                    contentDescription = "Inicio",
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "home") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = "Inicio",
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
                    contentDescription = "Gráficas",
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "charts") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = "Gráficas",
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
                        contentDescription = "Añadir",
                        tint = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )
        if (isSheetOpen)
            ModalBottomSheet(
                sheetState = sheetState,
                onDismissRequest = {
                    isSheetOpen = false
                },

            ) {
                var ingreso by rememberSaveable {
                    mutableStateOf(true)
                }
                var gasto by rememberSaveable {
                    mutableStateOf(false)
                }
                val gastos by categoriasViewModel.gastos.collectAsState()
                val ingresos by categoriasViewModel.ingresos.collectAsState()
                val colorTextoIngreso = if (isSystemInDarkTheme()) {
                    if (ingreso) MaterialTheme.colorScheme.primary else Color.Black
                } else {
                    if (ingreso) Color.Black else Color.Black
                }
                val colorTextoGasto = if (isSystemInDarkTheme()) {
                    if (gasto) Color.Cyan else Color.Black
                } else {
                    if (gasto) Color.Black else Color.Black
                }
                Log.d("DEBUG", "Gastos: ${gastos.size}, Ingresos: ${ingresos.size}")

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surface)

                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.surface)

                    ) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround

                            ) {

                                Button(
                                    onClick = {
                                        ingreso = true
                                        gasto = false
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (ingreso) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,

                                        )
                                ) {
                                    Text(
                                        text = "Ingresos",
                                        color = colorTextoIngreso
                                    )
                                }
                                Button(
                                    onClick = {
                                        gasto = true
                                        ingreso = false

                                    },

                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (gasto) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer,
                                        )
                                ) {
                                    Text(
                                        text = "Gastos",
                                        color = colorTextoGasto
                                    )
                                }
                                Button(
                                    modifier = Modifier
                                        .width(60.dp),
                                    onClick = {
                                        val tipo = if (ingreso) "ingreso" else "gasto"
                                        navigationController.navigate("crearCategoria/$tipo")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,

                                        )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = "Añadir",
                                        tint = Color.Black
                                    )
                                }
                            }
                            Spacer(
                                modifier = Modifier
                                    .size(30.dp)
                            )
                            if (ingreso) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(362.dp)
                                    ) {
                                        ingresos.forEach { categoria ->
                                            CategoriasItem(navigationController, categoria, sheetState, "ingreso")
                                        }

                                    }
                                }

                            } else if (gasto) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .width(362.dp)
                                    ) {
                                        gastos.forEach { categoria ->
                                            CategoriasItem(navigationController, categoria, sheetState, "gasto")
                                        }

                                    }
                                }
                            } else {
                                Spacer(
                                    modifier = Modifier
                                        .size(30.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(start = 20.dp, end = 20.dp, top = 30.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                ) {
                                    Text(
                                        text = "Seleccione las Categorias que desea ver con los botones superiores.",
                                        textAlign = TextAlign.Center

                                    )
                                }
                            }

                        }

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
                    contentDescription = "Metas",
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "budget") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = "Metas",
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
            selected = selectedIcon.value == "ajustes",
            onClick = {
                selectedIcon.value = "ajustes"
                navigationController.navigate(Pantallas.Ajustes.pantalla) {
                    popUpTo(0)
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "ajustes",
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "ajustes") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
                )
            },
            label = {
                Text(
                    text = "Ajustes",
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "ajustes") Color.White else MaterialTheme.colorScheme.onPrimaryContainer
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



