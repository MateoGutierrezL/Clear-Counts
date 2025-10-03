package com.example.clearcounts.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.Barras.DrawerItem
import com.example.clearcounts.ui.Barras.NavigationDrawer
import com.example.clearcounts.ui.Barras.TopBar
import com.example.clearcounts.ui.Inicio.HomeScreen
import com.example.clearcounts.ui.Perfil.EditarPerfil
import com.example.clearcounts.ui.Perfil.Perfil
import kotlinx.coroutines.launch

//Funcion que maneja el topappbar bottombar y la barra desplegable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation() {
    val navigationController = rememberNavController()
    val selectedIcon = remember { mutableStateOf("home") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomBarVisible = rememberSaveable { mutableStateOf(true) }

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
                    },
                    selectedIcon = selectedIcon,
                    navigationController = navigationController
                )
            },
            bottomBar = {
                if (bottomBarVisible.value) {
                    CustomBottomAppBar(
                        selectedIcon = selectedIcon,
                        navigationController = navigationController
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navigationController,
                startDestination = Pantallas.Inicio.pantalla,
                modifier = Modifier.padding(paddingValues)
            ) {

                composable(Pantallas.Inicio.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        onDispose {}
                    }

                    HomeScreen(paddingValues)
                }
                composable(Pantallas.Graficas.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        onDispose {}
                    }

                    Graficas()
                }
                composable(Pantallas.Perfil.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        onDispose {}
                    }

                    Perfil(navigationController)
                }
                composable(Pantallas.Presupuesto.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        onDispose {}
                    }

                    Presupuesto()
                }

                composable(Pantallas.EditarPerfil.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false // Ocultar barra inferior
                        onDispose {}
                    }

                    EditarPerfil(navigationController)
                }

            }
        }
    }
}