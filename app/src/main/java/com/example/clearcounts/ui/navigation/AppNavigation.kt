package com.example.clearcounts.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.Presupuesto
import com.example.clearcounts.ui.screens.Graficas
import com.example.clearcounts.ui.screens.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.screens.Barras.DrawerItem
import com.example.clearcounts.ui.screens.Barras.NavigationDrawer
import com.example.clearcounts.ui.screens.Barras.TopBar
import com.example.clearcounts.ui.screens.IngresosGastos.ingresos
import com.example.clearcounts.ui.screens.PreguntasComentarios.PreguntasComentarios
import com.example.clearcounts.ui.screens.Inicio.HomeScreen
import com.example.clearcounts.ui.screens.UserSessionViewModel
import com.example.clearcounts.ui.screens.Notificaciones.Notificaciones
import com.example.clearcounts.ui.screens.Perfil.EditarPerfil
import com.example.clearcounts.ui.screens.Perfil.Perfil
import kotlinx.coroutines.launch

//Funcion que maneja el topappbar bottombar y la barra desplegable
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    rootNavController: NavController,
    viewModel: UserSessionViewModel = hiltViewModel(),

) {
    val navigationController = rememberNavController()
    val selectedIcon = remember { mutableStateOf("home") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomBarVisible = rememberSaveable { mutableStateOf(true) }
    val topBarVisible = rememberSaveable { mutableStateOf(true) }
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()

    

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet (
                modifier = Modifier.width(290.dp) //Tamaño de la barra leteral desplegable
            ){
                NavigationDrawer(
                    profilePicture = painterResource(id = R.drawable.user),
                    name = currentUser?.nombre ?: "Usuario invitado",
                    email = currentUser?.correo ?: "No disponible",
                    items = DrawerItem.entries,
                ) {
                    when (it) {
                        DrawerItem.EXPORT_PDF -> {}
                        DrawerItem.PERSONALIZACION -> {}
                        DrawerItem.CONTACT -> {}
                        DrawerItem.TUTORIAL -> {}
                        DrawerItem.LOG_OUT -> {
                            viewModel.logOut{
                                rootNavController.navigate("InicioSesion"){
                                    popUpTo("Inicio"){
                                        inclusive = true
                                    }
                                }
                            }
                        }
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
                            navigationController.navigate(Pantallas.PreguntasComentarios.pantalla)
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
                            if (navigationController.previousBackStackEntry != null){
                                navigationController.popBackStack()
                            }
                        },
                        navegarPerfil = {
                            navigationController.navigate(Pantallas.Perfil.pantalla)
                        }
                    )
                }

                composable(Pantallas.PreguntasComentarios.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false //Aqui tambien ocultamos la barra inferior
                        topBarVisible.value = true
                        onDispose {}
                    }

                    PreguntasComentarios(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null){
                                navigationController.popBackStack()
                            }
                        }
                    )
                }

                composable(Pantallas.Notificaciones.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    Notificaciones()
                }

                composable(
                    route = "{ruta}/{icono}/{nombre}",
                    arguments = listOf(
                        navArgument("ruta") { type = NavType.StringType },
                        navArgument("icono") { type = NavType.IntType },
                        navArgument("nombre") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val ruta = backStackEntry.arguments?.getString("ruta") ?: "ingresos"
                    val icono = backStackEntry.arguments?.getInt("icono") ?: 0
                    val nombre = backStackEntry.arguments?.getString("nombre").orEmpty()

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {  }
                    }

                    ingresos(
                        ruta = ruta,
                        icono = icono,
                        nombre = nombre,
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null){
                                navigationController.popBackStack()
                            }
                        },
                        botonCrearNavegacion = {
                            navigationController.navigate(Pantallas.Inicio.pantalla)
                        }
                    )

                }

            }
        }
    }
}