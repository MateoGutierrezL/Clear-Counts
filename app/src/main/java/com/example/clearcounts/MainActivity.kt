package com.example.clearcounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.HomeScreen
import com.example.clearcounts.ui.MainScreen
import com.example.clearcounts.ui.PantallaInicioSesion
import com.example.clearcounts.ui.PantallaRegistro


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
            MainScreen()
        }


    }
}


