package com.example.clearcounts

import android.os.Bundle
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.navigation.AppNavigation
import com.example.clearcounts.ui.navigation.Pantallas
import com.example.clearcounts.ui.screens.Inicio.HomeScreen
import com.example.clearcounts.ui.screens.InicioSesion.PantallaInicioSesion
import com.example.clearcounts.ui.screens.InicioSesion.PantallaRegistro
import com.example.clearcounts.ui.theme.ClearCountTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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

        lifecycleScope.launch{
            val user = auth.currentUser
            isUserLoggedIn = user != null
            delay(1000)
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
                    rootNavController.navigate("RecuperarClave")
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

        composable ("Inicio"){
            AppNavigation(rootNavController = rootNavController)
        }
    }
}


