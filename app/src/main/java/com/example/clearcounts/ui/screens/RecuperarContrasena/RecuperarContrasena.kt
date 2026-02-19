package com.example.clearcounts.ui.screens.RecuperarContrasena

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clearcounts.ui.screens.Barras.TopBar
import com.example.clearcounts.ui.theme.AzulEncabezado
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.utils.OutlinedTextFieldColors
import dagger.hilt.android.lifecycle.HiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarContrasena(
    botonVolver:() -> Unit,
    onBotonSiguiente: () -> Unit,
    viewModel: RecuperarContrasenaViewModel = hiltViewModel()
){

    var correo by remember {mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            modifier = Modifier.padding(top = 8.dp, end = 14.dp),
                            text = "ClearCounts",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 30.sp
                        )
                    }
                },
                navigationIcon = {

                    IconButton(
                        onClick = {
                            botonVolver()
                        },
                        modifier = Modifier
                            .padding(top = 10.dp, start = 5.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Text(
                text = "Inicio de sesión",
                modifier = Modifier.padding(start = 16.dp, top = 24.dp),
                fontSize = 18.sp
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = "Busca tu dirección de \ncorreo electrónico",
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 15.dp)
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(start = 15.dp, end = 15.dp, top = 24.dp, bottom = 24.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Introduce tu dirección de correo electrónico de recuperación",
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 24.dp),
                fontSize = 18.sp
            )

            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                placeholder = { Text("Ingrese dirección de correo electrónico")},
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldColors(
                    unfocusedBorder = gris,
                    focusedBorder = gris
                ),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()
            )

            //Llamada del boton inferior
            BotonSiguiente(
                onBotonSiguiente = {
                    if (correo.isNotBlank()){

                        viewModel.sendEmailResetPassword(correo)
                        onBotonSiguiente()
                    }

                }
            )

            Spacer(modifier = Modifier.height(260.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

        }
    }
}

@Composable
fun BotonSiguiente(

    onBotonSiguiente:() -> Unit

){
    Button(onClick = {
            onBotonSiguiente()
    }, modifier = Modifier.padding(top = 32.dp, start = 250.dp).border(
        border = BorderStroke(1.dp, color = MaterialTheme.colorScheme.primary),
        shape = ButtonDefaults.shape,
    ),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background
        )) {

        Text("Siguiente", color = MaterialTheme.colorScheme.onBackground)

    }
}