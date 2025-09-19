package com.example.clearcounts.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val drawerState = rememberDrawerState(initialValue = androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerContent = {
            // Aquí puedes colocar el contenido de tu menú lateral

            NavigationDrawer(
                name = "Mateo Gutierrez",
                email = "mateo@gmail.com",
                items = DrawerItem.values().toList()) {

                when(it){
                    DrawerItem.ABOUT -> {

                        //Aqui se añade cada una de las rutas para los diferentes
                        //apartados de pantallas ya sea settings o recent y demas
                    }
                    DrawerItem.SETTINGS -> {

                    }
                    DrawerItem.RECENT -> {

                    }
                    DrawerItem.ACCOUNT -> {

                    }

                }
                scope.launch {
                    drawerState.close()
                }

            }
            Text("Contenido del menú lateral")
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            drawerState.apply { if (isClosed) open() else close() }
                        }
                    }
                )
            }
        ) { paddingValues ->
            HomeScreen(paddingValues)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = "Navigation Frawe 123")
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Menu Drawer"
                )
            }
        }
    )
}

@Composable
fun NavigationDrawer(
    name: String,
    email: String,
    items: List<DrawerItem>,
    modifier: Modifier = Modifier,
    onItemClick: (DrawerItem) -> Unit

){
    Column(modifier = Modifier.fillMaxWidth()){
        Text(text = name)
        Text(text = email)

        Spacer(modifier = Modifier.weight(1f))

        items.forEach {
            Row(modifier = Modifier.fillMaxWidth().clickable{
                onItemClick(it)
            }.padding(16.dp), verticalAlignment = CenterVertically ){
                Icon(imageVector = it.icon, contentDescription = it.text)
                Spacer(modifier = Modifier.width(32.dp))
                Text(text = it.text)
            } }
    }
}





