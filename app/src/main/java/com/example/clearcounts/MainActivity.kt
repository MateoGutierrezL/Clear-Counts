package com.example.clearcounts

import android.Manifest
import android.icu.util.Calendar
import android.icu.util.TimeUnit
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.clearcounts.ui.navigation.AppNavigation
import com.example.clearcounts.ui.navigation.Pantallas
import com.example.clearcounts.ui.screens.Ajustes.ThemeViewModel
import com.example.clearcounts.ui.screens.Inicio.HomeScreen
import com.example.clearcounts.ui.screens.InicioSesion.PantallaInicioSesion
import com.example.clearcounts.ui.screens.InicioSesion.PantallaRegistro
import com.example.clearcounts.ui.screens.InicioSesion.SyncState
import com.example.clearcounts.ui.screens.InicioSesion.SyncViewModel
import com.example.clearcounts.ui.screens.RecuperarContrasena.RecuperarCodigo
import com.example.clearcounts.ui.screens.RecuperarContrasena.RecuperarContrasena
import com.example.clearcounts.ui.screens.RecuperarContrasena.RecuperarVerificacionCorreo
import com.example.clearcounts.ui.theme.ClearCountTheme
import com.example.clearcounts.utils.DailyReminderWorker
import com.example.clearcounts.utils.NotificationHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

/*
Una buena app permite lo siguiente

No falle si el usuario recibe una llamada telefónica o cambia a otra app mientras usa la tuya.
No consuma recursos valiosos del sistema cuando el usuario no la use de forma activa.
No pierda el progreso del usuario si este abandona tu app y regresa a ella posteriormente.
No falle ni pierda el progreso del usuario cuando se gire la pantalla entre la orientación horizontal y la vertical.

 */

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        //Manejo de la pantalla de carga
        val splashScreen = installSplashScreen()
        var isUserLoggedIn by mutableStateOf<Boolean?>(null)

        super.onCreate(savedInstanceState)
        // Canal para las notificaciones
        NotificationHelper.createNotificationChannel(this)

        lifecycleScope.launch{
            val user = auth.currentUser
            isUserLoggedIn = user != null
            delay(1000)

            // Programar recordatorio diario solo si el usuario está logueado
            if (isUserLoggedIn == true) {
                scheduleDailyReminder()
            }
        }

        splashScreen.setKeepOnScreenCondition { isUserLoggedIn == null }

        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel by viewModels()
            val isDarkMode by themeViewModel.isDarkMode.collectAsStateWithLifecycle()

            ClearCountTheme(darkTheme = isDarkMode) {
                isUserLoggedIn?.let { loggedIn ->
                    InicioUsuario(
                        isLoggedIn = loggedIn,
                        themeViewModel = themeViewModel   // pásalo hacia abajo
                    )
                }
            }
        }
    }
    private fun scheduleDailyReminder() {
        val now = Calendar.getInstance()

        // Primera notificación - 8pm
        val target1 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 18)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        // Segunda notificación
        val target2 = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 12) // Cambia la hora aquí
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
            if (before(now)) add(Calendar.DAY_OF_MONTH, 1)
        }

        val workRequest1 = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            java.time.Duration.ofHours(24)
        )
            .setInitialDelay(java.time.Duration.ofMillis(target1.timeInMillis - now.timeInMillis))
            .setInputData(workDataOf(
                "titulo_key" to "no_olvides_registrar_tus_movimientos",
                "mensaje_key" to "lleva_un_control_de_tus_gastos_e_ingresos_de_hoy"
            ))
            .build()

        val workRequest2 = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            java.time.Duration.ofHours(24)
        )
            .setInitialDelay(java.time.Duration.ofMillis(target2.timeInMillis - now.timeInMillis))
            .setInputData(workDataOf(
                "titulo_key" to "c_mo_van_tus_finanzas_hoy",
                "mensaje_key" to "revisa_tu_resumen_del_d_a_en_clearcounts"
            ))
            .build()


        WorkManager.getInstance(this).apply {
            enqueueUniquePeriodicWork("daily_reminder_1", ExistingPeriodicWorkPolicy.REPLACE, workRequest1)
            enqueueUniquePeriodicWork("daily_reminder_2", ExistingPeriodicWorkPolicy.REPLACE, workRequest2)
        }

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("daily_reminder_1")
            .observe(this) { workInfos ->
                workInfos?.forEach { Log.d("WorkManager", "Reminder1 - Estado: ${it.state}") }
            }

        WorkManager.getInstance(this).getWorkInfosForUniqueWorkLiveData("daily_reminder_2")
            .observe(this) { workInfos ->
                workInfos?.forEach {
                    Log.d("WorkManager", "Reminder2 - Estado: ${it.state}")
                    if (it.state == WorkInfo.State.FAILED) {
                        Log.e("WorkManager", "Reminder2 - Error: ${it.outputData.keyValueMap}")
                    }
                }
            }
    }
}

@Composable
fun InicioUsuario(
    isLoggedIn: Boolean,
    themeViewModel: ThemeViewModel
){

    val rootNavController = rememberNavController()

    val rutaInicial = if (isLoggedIn) "Inicio" else "InicioSesion"

    NavHost(navController = rootNavController, startDestination = rutaInicial) {

        composable("InicioSesion")
        {
            PantallaInicioSesion(

                navegarRegistroUsuario = {
                    rootNavController.navigate("RegistroUsuario")
                },
                navegarInicio = {
                    rootNavController.navigate("Inicio"){

                        popUpTo("InicioSesion") {

                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                },
                navegarOlvidoContrasena = {
                    rootNavController.navigate("RecuperarContrasena")
                }
            )
        }

        composable("RegistroUsuario")
        {
            PantallaRegistro(

                navegarBotonRegistrarme = {

                    rootNavController.navigate("InicioSesion") {
                        popUpTo("PantallaRegistro") { inclusive = true }
                    }
                },
                textoNavegarInicioSesion = {

                    rootNavController.navigate("InicioSesion")
                }
            )
        }

        composable("RecuperarVerificacionCorreo") {

            RecuperarVerificacionCorreo(
                botonVolver = {
                    if (rootNavController.previousBackStackEntry != null){
                        rootNavController.popBackStack()
                    }
                },
                onBotonSiguienteVerificacion = {
                    //TODO falta implementar la logica para realizar con la recuperaccion de contraseña
                }
            )
        }

        composable("RecuperarCodigo"){

            RecuperarCodigo(
                botonVolver = {
                    if (rootNavController.previousBackStackEntry != null){
                        rootNavController.popBackStack()
                    }
                },
                onBotonSiguienteCodigo = {
                    rootNavController.navigate("RecuperarVerificacionCorreo")
                }
            )
        }

        composable("RecuperarContrasena"){

            RecuperarContrasena(

                botonVolver = {
                    if (rootNavController.previousBackStackEntry != null){
                        rootNavController.popBackStack()
                    }
                },
                onBotonSiguiente = {
                    rootNavController.navigate("RecuperarCodigo")
                }
            )
        }

        composable("Inicio") {
            val syncViewModel: SyncViewModel = hiltViewModel()
            val syncState by syncViewModel.syncState.collectAsState()

            // Sincroniza al entrar por primera vez
            LaunchedEffect(Unit) {
                syncViewModel.sincronizarDesdFirestore()
            }

            when (syncState) {
                is SyncState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Sincronizando datos...")
                        }
                    }
                }
                else -> {
                    // SyncState.Success, SyncState.Error o SyncState.Idle
                    // En cualquier caso mostramos la app normal
                    // Si hay error los datos locales de Room siguen disponibles
                    AppNavigation(
                        rootNavController = rootNavController,
                        themeViewModel = themeViewModel
                    )
                }
            }
        }
    }
}


