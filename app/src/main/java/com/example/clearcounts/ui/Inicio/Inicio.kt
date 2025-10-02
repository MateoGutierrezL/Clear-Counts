package com.example.clearcounts.ui.Inicio

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clearcounts.ui.Barras.CustomBottomAppBar
import com.example.clearcounts.ui.Barras.DrawerItem
import com.example.clearcounts.ui.Barras.NavigationDrawer
import com.example.clearcounts.ui.Barras.TopBar
import com.example.clearcounts.ui.Graficas
import com.example.clearcounts.ui.Pantallas
import com.example.clearcounts.ui.Perfil.EditarPerfil
import com.example.clearcounts.ui.Perfil.Perfil
import com.example.clearcounts.ui.Presupuesto
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(paddingValues: PaddingValues) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Esta es la pantalla de inicio")
    }
}











