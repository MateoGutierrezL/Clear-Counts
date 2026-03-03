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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.example.clearcounts.ui.screens.Inicio.HomeScreen
import com.example.clearcounts.ui.screens.InicioSesion.PantallaInicioSesion
import com.example.clearcounts.ui.screens.InicioSesion.PantallaRegistro
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
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        //Manejo de la pantalla de carga
        val splashScreen = installSplashScreen()
        var isUserLoggedIn by mutableStateOf<Boolean?>(null)

        super.onCreate(savedInstanceState)
        // Canal para las notificaciones
        NotificationHelper.createNotificationChannel(this)

        // Pedir permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

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
            ClearCountTheme(darkTheme = isSystemInDarkTheme()) {
                isUserLoggedIn?.let { loggedIn ->
                    InicioUsuario(isLoggedIn = loggedIn )
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
                "titulo" to "¡No olvides registrar tus movimientos!",
                "mensaje" to "Lleva un control de tus gastos e ingresos de hoy."
            ))
            .build()

        val workRequest2 = PeriodicWorkRequestBuilder<DailyReminderWorker>(
            java.time.Duration.ofHours(24)
        )
            .setInitialDelay(java.time.Duration.ofMillis(target2.timeInMillis - now.timeInMillis))
            .setInputData(workDataOf(
                "titulo" to "¿Cómo van tus finanzas hoy?",
                "mensaje" to "Revisa tu resumen del día en ClearCounts."
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
fun InicioUsuario(isLoggedIn: Boolean){

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

        composable ("Inicio"){
            AppNavigation(rootNavController = rootNavController)
        }
    }
}


