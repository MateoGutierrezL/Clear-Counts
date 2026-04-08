package com.example.clearcounts.ui.navigation

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
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
import com.example.clearcounts.ui.screens.Ajustes.Ajustes
import com.example.clearcounts.ui.screens.Ajustes.LocaleManager
import com.example.clearcounts.ui.screens.Ajustes.SeleccionarIdioma
import com.example.clearcounts.ui.screens.Ajustes.TerminosCondiciones
import com.example.clearcounts.ui.screens.Ajustes.ThemeViewModel
import com.example.clearcounts.ui.screens.graficas.Graficas
import com.example.clearcounts.ui.screens.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.screens.Barras.DrawerItem
import com.example.clearcounts.ui.screens.Barras.NavigationDrawer
import com.example.clearcounts.ui.screens.Barras.TopBar
import com.example.clearcounts.ui.screens.Categorias.crearCategoria
import com.example.clearcounts.ui.screens.Contactos.Contactos
import com.example.clearcounts.ui.screens.Contactos.PoliticasPrivacidad
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
import com.example.clearcounts.ui.screens.Metas.TipoBudget
import com.example.clearcounts.ui.screens.UserSessionViewModel
import com.example.clearcounts.ui.screens.Notificaciones.Notificaciones
import com.example.clearcounts.ui.screens.Notificaciones.NotificacionesViewModel
import com.example.clearcounts.ui.screens.Perfil.CambiarContrasena
import com.example.clearcounts.ui.screens.Perfil.Perfil
import com.example.clearcounts.ui.screens.Recurrentes.CrearRecurrente
import com.example.clearcounts.ui.screens.Recurrentes.EditarRecurrente
import com.example.clearcounts.ui.screens.Recurrentes.Recurrentes
import com.example.clearcounts.ui.screens.Recurrentes.RecurringViewModel
import com.example.clearcounts.utils.LoadingOverlay
import kotlinx.coroutines.launch

@SuppressLint("LocalContextGetResourceValueCall")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    rootNavController: NavController,
    viewModel: UserSessionViewModel = hiltViewModel(),
    notificacionesViewModel: NotificacionesViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel

) {

    val isLoggingOut by viewModel.isLoggingOut.collectAsStateWithLifecycle()
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
            ModalDrawerSheet(
                modifier = Modifier.width(290.dp) //Tamaño de la barra leteral desplegable
            ) {
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

                        DrawerItem.AJUSTES -> {
                            navigationController.navigate(Pantallas.Ajustes.pantalla)
                        }

                        DrawerItem.EXPORT_PDF -> {
                            navigationController.navigate(Pantallas.Exportar.pantalla)
                        }

                        DrawerItem.CONTACT -> {
                            navigationController.navigate(Pantallas.Contactos.pantalla)
                        }

                        DrawerItem.TUTORIAL -> {}
                        DrawerItem.LOG_OUT -> {
                            viewModel.logOut {
                                rootNavController.navigate("InicioSesion") {
                                    popUpTo("Inicio") {
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

                    HomeScreen(
                        navegarTodasTransacciones = {
                            navigationController.navigate(Pantallas.TodasTransacciones.pantalla)
                        },
                        navegarTransaccionesPorMetodo = { metodo ->
                            navigationController.navigate(
                                Pantallas.TransaccionesPorMetodo.createRoute(
                                    metodo
                                )
                            )
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
                        navegarCambiarContrasena = {
                            navigationController.navigate(Pantallas.CambiarContrasena.pantalla)
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
                            navigationController.navigate(
                                Pantallas.Detalle.createRoute(
                                    tipoSeleccionado
                                )
                            )
                        },
                        onVerDetalle = { budget ->
                            navigationController.navigate(Pantallas.DetalleBudget.createRoute(budget.firestoreId))
                        }
                    )
                }

                composable(Pantallas.DetalleBudget.pantalla) { backStackEntry ->

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }


                    val viewModelMetas: MetasViewModel = hiltViewModel()
                    val allBudgets by viewModelMetas.allBudgets.collectAsState()
                    val budgetId = backStackEntry.arguments?.getString("budgetId")
                    val budget = allBudgets.find { it.firestoreId == budgetId }

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
                                    Pantallas.Detalle.createRoute(
                                        budgetAEditar.tipo,
                                        budgetAEditar.firestoreId
                                    )
                                )
                            }
                        )
                    }
                }

                composable(Pantallas.CambiarContrasena.pantalla) {
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    CambiarContrasena(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
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

                    PantallaExportarGrafico(themeViewModel = themeViewModel)
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
                    val metodoPago =
                        backStackEntry.arguments?.getString("metodoPago") ?: "Efectivo" // 👈

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
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) { backStackEntry ->
                    val tipo = backStackEntry.arguments?.getString("tipo") ?: TipoBudget.META
                    val budgetId = backStackEntry.arguments?.getString("budgetId") ?: ""

                    val viewModelMetas: MetasViewModel = hiltViewModel()
                    val allBudgets by viewModelMetas.allBudgets.collectAsState()
                    val budgetAEditar = allBudgets.find { it.firestoreId == budgetId }

                    val (tituloFinal, iconoFinal) = when (tipo) {
                        TipoBudget.DEUDA -> stringResource(R.string.tab_deudas) to Icons.Default.ErrorOutline
                        TipoBudget.TE_DEBEN -> stringResource(R.string.te_deben) to Icons.Default.VerifiedUser
                        else -> stringResource(R.string.tab_metas) to Icons.Default.TrackChanges
                    }

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }

                    Prestamo(
                        titulo = tituloFinal,
                        tipoOriginal = tipo,
                        icono = iconoFinal,
                        budgetAEditar = budgetAEditar,
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                            navigationController.popBackStack()
                        } }
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
                        navegarTerminosCondiciones = {
                            navigationController.navigate(Pantallas.TerminosCondiciones.pantalla)
                        },
                        navegarContacto = {
                            navigationController.navigate(Pantallas.Contactos.pantalla)
                        },
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

                composable(Pantallas.Recurrentes.pantalla) {
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = true
                        topBarVisible.value = true
                        onDispose {}
                    }
                    Recurrentes(
                        onBotonCrear = {
                            navigationController.navigate(Pantallas.CrearRecurrente.pantalla)
                        },
                        onEditar = { recurrente ->
                            navigationController.navigate(
                                Pantallas.EditarRecurrente.createRoute(recurrente.firestoreId)
                            )
                        }
                    )
                }

                composable(Pantallas.CrearRecurrente.pantalla) {
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    CrearRecurrente(
                        botonVolver = { if (navigationController.previousBackStackEntry != null) {
                            navigationController.popBackStack()
                        } }
                    )
                }

                composable(
                    route = Pantallas.EditarRecurrente.pantalla,
                    arguments = listOf(navArgument("firestoreId") { type = NavType.StringType })
                ) { backStackEntry ->
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    val firestoreId = backStackEntry.arguments?.getString("firestoreId") ?: ""
                    val viewModelRecurrente: RecurringViewModel = hiltViewModel()
                    val todosLosRecurrentes by viewModelRecurrente.ingresos.collectAsState()
                    val todosLosGastos by viewModelRecurrente.gastos.collectAsState()
                    val item = (todosLosRecurrentes + todosLosGastos).find { it.firestoreId == firestoreId }

                    if (item != null) {
                        EditarRecurrente(
                            item = item,
                            onGuardar = { actualizado ->
                                viewModelRecurrente.actualizar(actualizado)
                                navigationController.popBackStack()
                            },
                            onVolver = { if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            } }
                        )
                    }
                }

                composable(Pantallas.Contactos.pantalla) {

                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }

                    Contactos(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null) {
                                navigationController.popBackStack()
                            }
                        },
                        navegarPoliticasPrivacidad = {
                            navigationController.navigate(Pantallas.PoliticasPrivacidad.pantalla)
                        }
                    )
                }


                composable(Pantallas.TerminosCondiciones.pantalla) {
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    TerminosCondiciones(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null)
                                navigationController.popBackStack()
                        }
                    )
                }

                composable(Pantallas.PoliticasPrivacidad.pantalla) {
                    DisposableEffect(Unit) {
                        bottomBarVisible.value = false
                        topBarVisible.value = false
                        onDispose {}
                    }
                    PoliticasPrivacidad(
                        botonVolver = {
                            if (navigationController.previousBackStackEntry != null)
                                navigationController.popBackStack()
                        }
                    )
                }


            }


        }
    }
    LoadingOverlay(
        visible = isLoggingOut,
        mensaje = stringResource(R.string.cerrando_sesion)
    )

}
