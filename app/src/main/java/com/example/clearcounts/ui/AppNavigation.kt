package com.example.clearcounts.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.R
import com.example.clearcounts.ui.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.Barras.DrawerItem
import com.example.clearcounts.ui.Barras.NavigationDrawer
import com.example.clearcounts.ui.Barras.TopBar
import com.example.clearcounts.ui.PreguntasComentarios.PreguntasComentarios
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
    val topBarVisible = rememberSaveable { mutableStateOf(true) }

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(290.dp) //Tamaño de la barra leteral desplegable
            ){
                NavigationDrawer(
                    profilePicture = painterResource(id = R.drawable.user),
                    name = "Mateo Gutierrez",
                    email = "mateo@gmail.com",
                    items = DrawerItem.entries,
                ) {
                    when (it) {
                        DrawerItem.EXPORT_PDF -> {}
                        DrawerItem.PERSONALIZACION -> {}
                        DrawerItem.CONTACT -> {}
                        DrawerItem.TUTORIAL -> {}
                        DrawerItem.LOG_OUT -> {}
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
                if (topBarVisible.value) {
                    TopBar(
                        onMenuClick = {
                            scope.launch {
                                drawerState.apply { if (isClosed) open() else close() }
                            }
                        },
                        selectedIcon = selectedIcon,
                        navegarPantallaNotificaciones = {

                            navigationController.navigate(Pantallas.Notificaciones.pantalla) {
                                popUpTo(0)
                            }
                        },
                        navegarPantallaPreguntasComentarios = {
                            navigationController.navigate(Pantallas.PreguntasComentarios.pantalla) {
                                popUpTo(0)
                            }
                        }
                    )
                }
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
                        topBarVisible.value = true
                        onDispose {}
                    }

                    HomeScreen(paddingValues)
                }
                composable(Pantallas.Graficas.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    Graficas()
                }

                composable(Pantallas.Perfil.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    Perfil(
                        navegarPantallaEditarPerfil = {
                            navigationController.navigate(Pantallas.EditarPerfil.pantalla)
                        }
                    )
                }
                composable(Pantallas.Presupuesto.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    Presupuesto()
                }

                composable(Pantallas.EditarPerfil.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false // Ocultar barra inferior
                        topBarVisible.value = true
                        onDispose {}
                    }

                    EditarPerfil(
                        botonVolver = {
                            navigationController.popBackStack()
                        }
                    )
                }

                composable(Pantallas.PreguntasComentarios.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false //Aqui tambien ocultamos la barra inferior
                        topBarVisible.value = true
                        onDispose {}
                    }

                    PreguntasComentarios()
                }

                composable(Pantallas.Notificaciones.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    Notificaciones()
                }

            }
        }
    }
}