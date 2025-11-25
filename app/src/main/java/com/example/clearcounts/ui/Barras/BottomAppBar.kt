package com.example.clearcounts.ui.Barras

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.clearcounts.ui.Pantallas
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
import androidx.navigation.NavHostController
import com.example.clearcounts.R
import com.example.clearcounts.ui.CategoriaItemGastos
import com.example.clearcounts.ui.CategoriaItemIngresos
import com.example.clearcounts.ui.DataSource
import com.example.clearcounts.ui.theme.AzulEncabezado


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomAppBar(
    selectedIcon: MutableState<String>, // Cambiado a String para identificar iconos
    navigationController: NavHostController
) {
    val context = LocalContext.current.applicationContext

    BottomAppBar(containerColor = AzulEncabezado) {
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
                    tint = if (selectedIcon.value == "home") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    text = "Inicio",
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "home") Color.White else Color.Black
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Black,
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
                    tint = if (selectedIcon.value == "charts") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    text = "Gráficas",
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "charts") Color.White else Color.Black
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Black,
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
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir",
                        tint = Color.White,
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
                    mutableStateOf(false)
                }
                var gasto by rememberSaveable {
                    mutableStateOf(false)
                }
                val colorTextoIngreso = if (isSystemInDarkTheme()) {
                    if (ingreso) Color.Cyan else Color.Black
                } else {
                    if (ingreso) Color.Black else Color.Black
                }
                val colorTextoGasto = if (isSystemInDarkTheme()) {
                    if (gasto) Color.Cyan else Color.Black
                } else {
                    if (gasto) Color.Black else Color.Black
                }
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
                                        // Futura navegacion a la pantalla de gastos o ingresos
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
                                        DataSource.categoriasIngresos.forEach { categoria ->
                                            CategoriaItemIngresos(
                                                navController = navigationController,
                                                categoria = categoria,
                                                sheetState = sheetState
                                            )
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
                                        DataSource.categoriasGastos.forEach { categoria ->
                                            CategoriaItemGastos(
                                                navController = navigationController,
                                                categoria = categoria,
                                                sheetState = sheetState
                                            )
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
                    painter = painterResource(id = R.drawable.save_money),
                    contentDescription = "Presupuesto",
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "budget") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    text = "Presupuesto",
                    fontSize = 11.sp,
                    color = if (selectedIcon.value == "budget") Color.White else Color.Black
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Black,
                indicatorColor = Color.Transparent
            )
        )

        // Perfil
        NavigationBarItem(
            selected = selectedIcon.value == "profile",
            onClick = {
                selectedIcon.value = "profile"
                navigationController.navigate(Pantallas.Perfil.pantalla) {
                    popUpTo(0)
                }
            },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.user),
                    contentDescription = "Perfil",
                    modifier = Modifier.size(26.dp),
                    tint = if (selectedIcon.value == "profile") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    text = "Perfil",
                    fontSize = 12.sp,
                    color = if (selectedIcon.value == "profile") Color.White else Color.Black
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                unselectedIconColor = Color.Black,
                selectedTextColor = Color.White,
                unselectedTextColor = Color.Black,
                indicatorColor = Color.Transparent
            )
        )
    }
}

