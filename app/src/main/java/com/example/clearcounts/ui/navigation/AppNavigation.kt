package com.example.clearcounts.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.TrackChanges
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import androidx.navigation.navigation
import com.example.clearcounts.R
import com.example.clearcounts.ui.screens.Ajustes.Ajustes
import com.example.clearcounts.ui.screens.Ajustes.LocaleManager
import com.example.clearcounts.ui.screens.Ajustes.SeleccionarIdioma
import com.example.clearcounts.ui.screens.Ajustes.ThemeViewModel
import com.example.clearcounts.ui.screens.Ajustes.idiomasDisponibles
import com.example.clearcounts.ui.screens.graficas.Graficas
import com.example.clearcounts.ui.screens.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.screens.Barras.DrawerItem
import com.example.clearcounts.ui.screens.Barras.NavigationDrawer
import com.example.clearcounts.ui.screens.Barras.TopBar
import com.example.clearcounts.ui.screens.Categorias.crearCategoria
import com.example.clearcounts.ui.screens.Exportar.PantallaExportarGrafico
import com.example.clearcounts.ui.screens.IngresosGastos.ingresos
import com.example.clearcounts.ui.screens.PreguntasComentarios.PreguntasComentarios
import com.example.clearcounts.ui.screens.Inicio.HomeScreen
import com.example.clearcounts.ui.screens.Inicio.TodasTransacciones
import com.example.clearcounts.ui.screens.Inicio.TransaccionesPorMetodo
import com.example.clearcounts.ui.screens.Metas.DetalleBudget
import com.example.clearcounts.ui.screens.Metas.Metas
import com.example.clearcounts.ui.screens.Metas.MetasViewModel
import com.example.clearcounts.ui.screens.Metas.Prestamo
import com.example.clearcounts.ui.screens.UserSessionViewModel
import com.example.clearcounts.ui.screens.Notificaciones.Notificaciones
import com.example.clearcounts.ui.screens.Notificaciones.NotificacionesViewModel
import com.example.clearcounts.ui.screens.Perfil.EditarPerfil
import com.example.clearcounts.ui.screens.Perfil.Perfil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    rootNavController: NavController,
    viewModel: UserSessionViewModel = hiltViewModel(),
    notificacionesViewModel: NotificacionesViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel

) {
    val navigationController = rememberNavController()
    val selectedIcon = remember { mutableStateOf("home") }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val bottomBarVisible = rememberSaveable { mutableStateOf(true) }
    val topBarVisible = rememberSaveable { mutableStateOf(true) }
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    val tieneNoLeidas by notificacionesViewModel.tieneNoLeidas.collectAsState()

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
                        DrawerItem.PERFIL -> {

                            navigationController.navigate(Pantallas.Perfil.pantalla)

                        }
                        DrawerItem.EXPORT_PDF -> {

                            navigationController.navigate(Pantallas.Exportar.pantalla)
                        }
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
                        tieneNotificaciones = tieneNoLeidas,
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

                    HomeScreen(navegarTodasTransacciones = {
                        navigationController.navigate(Pantallas.TodasTransacciones.pantalla)
                    },
                        navegarTransaccionesPorMetodo = { metodo ->
                            navigationController.navigate(Pantallas.TransaccionesPorMetodo.createRoute(metodo))
                        })
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

                    Metas(
                        onBotonCrear = { tipoSeleccionado ->
                            navigationController.navigate(Pantallas.Detalle.createRoute(tipoSeleccionado))
                        },
                        onVerDetalle = { budget ->
                            navigationController.navigate(Pantallas.DetalleBudget.createRoute(budget.id))
                        }
                    )
                }

                composable(Pantallas.DetalleBudget.pantalla) { backStackEntry ->

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }

                    val budgetId = backStackEntry.arguments?.getString("budgetId")?.toIntOrNull()
                    val viewModelMetas: MetasViewModel = hiltViewModel()
                    val allBudgets by viewModelMetas.allBudgets.collectAsState()
                    val budget = allBudgets.find { it.id == budgetId }

                    if (budget != null) {
                        DetalleBudget(
                            budget = budget,
                            onVolver = {
                                if (navigationController.previousBackStackEntry != null) {
                                    navigationController.popBackStack()
                                }
                            },
                            onEditar = { budgetAEditar ->
                                navigationController.navigate(
                                    Pantallas.Detalle.createRoute(budgetAEditar.tipo, budgetAEditar.id)
                                )
                            }
                        )
                    }
                }

                composable(Pantallas.EditarPerfil.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false // Ocultar barra inferior
                        topBarVisible.value = true
                        onDispose {}
                    }

                    EditarPerfil(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
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
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        }
                    )
                }

                composable(Pantallas.Exportar.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    PantallaExportarGrafico()
                }

                composable(Pantallas.Notificaciones.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        notificacionesViewModel.marcarTodasLeidas()
                        onDispose {}
                    }

                    Notificaciones(viewModel = notificacionesViewModel)

                }

                composable(
                    route = "{ruta}/{icono}/{nombre}/{metodoPago}",
                    arguments = listOf(
                        navArgument("ruta") { type = NavType.StringType },
                        navArgument("icono") { type = NavType.StringType },
                        navArgument("nombre") { type = NavType.StringType },
                        navArgument("metodoPago") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val ruta = backStackEntry.arguments?.getString("ruta") ?: "ingreso"
                    val icono = backStackEntry.arguments?.getString("icono") ?: ""
                    val nombre = backStackEntry.arguments?.getString("nombre").orEmpty()
                    val metodoPago = backStackEntry.arguments?.getString("metodoPago") ?: "Efectivo" // 👈

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose { }
                    }

                    ingresos(
                        ruta = ruta,
                        icono = icono,
                        nombre = nombre,
                        metodoPago = metodoPago, // 👈
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        },
                        botonCrearNavegacion = {
                            navigationController.navigate(Pantallas.Inicio.pantalla)
                        }
                    )
                }

                composable(
                    route = "crearCategoria/{tipo}",
                    arguments = listOf(navArgument("tipo") { type = NavType.StringType })
                ) { backStackEntry ->
                    val tipo = backStackEntry.arguments?.getString("tipo") ?: "ingreso"
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    crearCategoria(
                        tipo = tipo,
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        }
                    )
                }
                composable(
                    route = Pantallas.Detalle.pantalla,
                    arguments = listOf(
                        navArgument("tipo") { type = NavType.StringType },
                        navArgument("budgetId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) { backStackEntry ->
                    val tipo = backStackEntry.arguments?.getString("tipo") ?: "Metas"
                    val budgetId = backStackEntry.arguments?.getInt("budgetId") ?: -1

                    val viewModelMetas: MetasViewModel = hiltViewModel()
                    val allBudgets by viewModelMetas.allBudgets.collectAsState()
                    val budgetAEditar = allBudgets.find { it.id == budgetId }

                    val context = LocalContext.current

                    val (tituloFinal, iconoFinal) = when(tipo) {
                        "Deuda"    -> context.getString(R.string.tab_deudas) to Icons.Default.ErrorOutline
                        "Te deben" -> context.getString(R.string.te_deben)   to Icons.Default.VerifiedUser
                        else       -> context.getString(R.string.tab_metas)  to Icons.Default.TrackChanges
                    }

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }

                    Prestamo(
                        titulo = tituloFinal,
                        icono = iconoFinal,
                        budgetAEditar = budgetAEditar,  // <- pasa el budget
                        botonVolver = { navigationController.popBackStack() }
                    )

                }

                composable(Pantallas.TodasTransacciones.pantalla) {
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    TodasTransacciones(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        }
                    )
                }

                composable(Pantallas.Ajustes.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }

                    Ajustes(
                        navegarIdioma = {
                            navigationController.navigate(Pantallas.Idiomas.pantalla)
                        },
                        navegarPerfil = {
                            navigationController.navigate(Pantallas.Perfil.pantalla)
                        },
                        navegarExportar = {
                            navigationController.navigate(Pantallas.Exportar.pantalla)
                        },
                        onLogOut = {
                            viewModel.logOut {
                                rootNavController.navigate("InicioSesion") {
                                    popUpTo("Inicio") { inclusive = true }
                                }
                            }
                        },
                        themeViewModel = themeViewModel
                    )

                }

                composable(Pantallas.Idiomas.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }

                    val context = LocalContext.current

                    SeleccionarIdioma(
                        idiomaSeleccionado = LocaleManager.getCurrentLocaleTag(context),
                        onIdiomaSeleccionado = { idioma ->
                            LocaleManager.setLocale(context, idioma.codigo)
                        },
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        }
                    )
                }

                composable(
                    route = Pantallas.TransaccionesPorMetodo.pantalla,
                    arguments = listOf(navArgument("metodoPago") { type = NavType.StringType })
                ) { backStackEntry ->
                    val metodoPago = backStackEntry.arguments?.getString("metodoPago") ?: "Efectivo"

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }

                    TransaccionesPorMetodo(
                        metodoPago = metodoPago,
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        }
                    )
                }
            }
        }
    }
}
