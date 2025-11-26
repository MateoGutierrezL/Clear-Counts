package com.example.clearcounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.navigation.AppNavigation
import com.example.clearcounts.ui.screens.InicioSesion.PantallaInicioSesion
import com.example.clearcounts.ui.screens.InicioSesion.PantallaRegistro
import com.example.clearcounts.ui.theme.ClearCountTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        //Manejo de la pantalla de carga
        val splashScreen = installSplashScreen()
        var showSplashScreen = true
        splashScreen.setKeepOnScreenCondition { showSplashScreen }

        lifecycleScope.launch{
            delay(2000)
            showSplashScreen = false
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClearCountTheme(darkTheme = isSystemInDarkTheme()) {
                InicioUsuario()
            }
        }
    }
}

@Composable
fun InicioUsuario(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "InicioSesion") {

        composable("InicioSesion")
        {
            PantallaInicioSesion(

                navegarRegistroUsuario = {
                    navController.navigate("RegistroUsuario")
                },
                navegarInicio = {
                    navController.navigate("Inicio")
                },
                navegarOlvidoContrasena = {
                    navController.navigate("RecuperarClave")
                }
            )
        }

        composable("RegistroUsuario")
        {
            PantallaRegistro(

                navegarBotonRegistrarme = {

                    navController.navigate("InicioSesion") {
                        popUpTo("PantallaRegistro") { inclusive = true }
                    }
                },
                textoNavegarInicioSesion = {

                    navController.navigate("InicioSesion")
                }
            )
        }

        composable ("Inicio"){
            AppNavigation()
        }
    }
}


