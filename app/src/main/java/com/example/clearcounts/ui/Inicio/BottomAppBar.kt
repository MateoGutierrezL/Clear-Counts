package com.example.clearcounts.ui.Inicio

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.Pantallas
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clearcounts.ui.Graficas
import com.example.clearcounts.ui.Perfil
import com.example.clearcounts.ui.Presupuesto
import com.example.clearcounts.ui.theme.AzulClear

/*
@Composable
fun BottomAppBar() {
    // Componentes de la Vista y Estado
    val navigationController = rememberNavController()
    val context = LocalContext.current.applicationContext

    // Estado para el ícono seleccionado
    val selected = remember {
        mutableStateOf(Icons.Default.Home)
    }

    Scaffold(
        bottomBar = {
            // Contenedor de la Barra Inferior
            BottomAppBar {

                // === Destino 1: HOME (Inicio) ===
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Home
                        navigationController.navigate(Pantallas.Inicio.pantalla) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Home) Color.White else Color.DarkGray
                    )
                }

                // === Destino 2: SEARCH (Gráficas) ===
                IconButton(
                    onClick = {
                        selected.value = Icons.Default.Search
                        navigationController.navigate(Pantallas.Graficas.pantalla) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (selected.value == Icons.Default.Search) Color.White else Color.DarkGray
                    )
                }

                // === Floating Action Button (FAB) en el Centro ===
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = {
                            // Acción corregida: Muestra un mensaje de texto simple.
                            Toast.makeText(
                                context,
                                "Botón de Añadir presionado", // Mensaje de texto (String)
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    ) {
                        // Contenido del FAB: El Ícono que debe mostrar
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir",
                            tint = Color.White // El tint del ícono en un FAB suele ser blanco
                        )
                    }
                }

                // Nota: Aquí faltan los IconButtons para Perfil y Presupuesto
                // para que correspondan con los destinos del NavHost.

            } // Cierre de BottomAppBar
        } // Cierre de bottomBar
    ) { paddingValues ->

        // Contenedor de Navegación (NavHost)
        NavHost(
            navController = navigationController,
            startDestination = Pantallas.Inicio.pantalla,
            modifier = Modifier.padding(paddingValues)
        ) {
            // Definición de Destinos
            composable(Pantallas.Inicio.pantalla) { MainScreen() }
            composable(Pantallas.Graficas.pantalla) { Graficas() }
            composable(Pantallas.Perfil.pantalla) { Perfil() }
            composable(Pantallas.Presupuesto.pantalla) { Presupuesto() }
        }
    } // Cierre de Scaffold
}*/
@Composable
fun CustomBottomAppBar(
    selectedIcon: MutableState<ImageVector>,
    navigationController: NavHostController
) {
    val context = LocalContext.current.applicationContext

    BottomAppBar {
        //Home
        IconButton(
            onClick = {
                selectedIcon.value = Icons.Default.Home
                navigationController.navigate(Pantallas.Inicio.pantalla) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedIcon.value == Icons.Default.Home) Color.White else Color.DarkGray
            )
        }

        //Graficas
        IconButton(
            onClick = {
                selectedIcon.value = Icons.Default.Search
                navigationController.navigate(Pantallas.Graficas.pantalla) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedIcon.value == Icons.Default.Search) Color.White else Color.DarkGray
            )
        }

        // === Floating Action Button (FAB) en el Centro ===
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = {
                    Toast.makeText(
                        context,
                        "Botón de Añadir presionado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Añadir",
                    tint = Color.White
                )
            }
        }

        //Presupuesto
        IconButton(
            onClick = {
                selectedIcon.value = Icons.Default.Done
                navigationController.navigate(Pantallas.Presupuesto.pantalla) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedIcon.value == Icons.Default.Done) Color.White else Color.DarkGray
            )
        }

        //Perfil
        IconButton(
            onClick = {
                selectedIcon.value = Icons.Default.Person
                navigationController.navigate(Pantallas.Perfil.pantalla) {
                    popUpTo(0)
                }
            },
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(26.dp),
                tint = if (selectedIcon.value == Icons.Default.Person) Color.White else Color.DarkGray
            )
        }
    }
}