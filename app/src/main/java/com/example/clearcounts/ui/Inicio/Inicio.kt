package com.example.clearcounts.ui.Inicio

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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.MaterialTheme
import com.example.clearcounts.ui.Graficas
import com.example.clearcounts.ui.Inicio.DrawerItem
import com.example.clearcounts.ui.Pantallas
import com.example.clearcounts.ui.Perfil
import com.example.clearcounts.ui.Presupuesto
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Esta es la pantalla de inicio")
    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                // Llama al componente que creaste, que ahora manejará todo el contenido
                NavigationDrawer(
                    name = "Mateo Gutierrez",
                    email = "mateo@gmail.com",
                    items = DrawerItem.values().toList(),

                ) {
                    when (it) {
                        DrawerItem.ABOUT -> {}
                        DrawerItem.SETTINGS -> {}
                        DrawerItem.RECENT -> {}
                        DrawerItem.ACCOUNT -> {}
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            }
        ) { paddingValues ->
            HomeScreen(paddingValues)
        }
    }
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawer(
                    name = "Mateo Gutierrez",
                    email = "mateo@gmail.com",
                    items = DrawerItem.entries,
                ) {
                    when (it) {
                        DrawerItem.ABOUT -> {}
                        DrawerItem.SETTINGS -> {}
                        DrawerItem.RECENT -> {}
                        DrawerItem.ACCOUNT -> {}
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            }
        ) { paddingValues ->
            HomeScreen(paddingValues)
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navigationController = rememberNavController()
    val selectedIcon = remember { mutableStateOf(Icons.Default.Home) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawer(
                    name = "Mateo Gutierrez",
                    email = "mateo@gmail.com",
                    items = DrawerItem.entries,
                ) {
                    when (it) {
                        DrawerItem.ABOUT -> {}
                        DrawerItem.SETTINGS -> {}
                        DrawerItem.RECENT -> {}
                        DrawerItem.ACCOUNT -> {}
                    }
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            },
            bottomBar = {
                CustomBottomAppBar(
                    selectedIcon = selectedIcon,
                    navigationController = navigationController
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navigationController,
                startDestination = Pantallas.Inicio.pantalla,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Pantallas.Inicio.pantalla) { HomeScreen(paddingValues) }
                composable(Pantallas.Graficas.pantalla) { Graficas() }
                composable(Pantallas.Perfil.pantalla) { Perfil() }
                composable(Pantallas.Presupuesto.pantalla) { Presupuesto() }
            }
        }
    }
}










