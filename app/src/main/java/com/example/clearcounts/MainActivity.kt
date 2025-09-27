package com.example.clearcounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.Inicio.AppNavigation
import com.example.clearcounts.ui.InicioSesion.PantallaInicioSesion
import com.example.clearcounts.ui.InicioSesion.PantallaRegistro
import com.example.clearcounts.ui.Pantallas
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


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
            InicioUsuario()
        }
    }
}

@Composable
fun InicioUsuario(){

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "InicioSesion") {

        composable("InicioSesion")
        {
            PantallaInicioSesion(navController)
        }

        composable("RegistroUsuario")
        {
            PantallaRegistro(navController)
        }

        composable ("Inicio"){
            AppNavigation()
        }
    }
}


