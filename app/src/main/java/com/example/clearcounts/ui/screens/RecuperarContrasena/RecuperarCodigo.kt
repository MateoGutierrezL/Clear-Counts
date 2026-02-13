package com.example.clearcounts.ui.screens.RecuperarContrasena

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.clearcounts.ui.theme.gris
import com.example.clearcounts.utils.OutlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecuperarCodigo(
    botonVolver:() -> Unit,
    onBotonSiguienteCodigo:() -> Unit
){

    var codigo by remember {mutableStateOf("")}

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
    ){paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {

            Text(
                text = "Inicio de sesión",
                modifier = Modifier.padding(start = 16.dp, top = 24.dp),
                fontSize = 18.sp
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(42.dp))

            Text(
                text = "Introducir codigo de \nverificación",
                fontWeight = FontWeight.Medium,
                fontSize = 24.sp,
                modifier = Modifier.padding(start = 15.dp)
            )

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp, top = 24.dp, bottom = 24.dp),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Se acaba de enviar un codigo de verificación a (correo debe aparecer aquí)",
                modifier = Modifier.padding(start = 15.dp, end = 15.dp, bottom = 24.dp),
                fontSize = 18.sp
            )

            OutlinedTextField(
                value = codigo,
                onValueChange = { codigo = it },
                placeholder = { Text("Ingrese codigo")},
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldColors(
                    unfocusedBorder = gris,
                    focusedBorder = gris
                ),
                modifier = Modifier.padding(start = 15.dp, end = 15.dp).fillMaxWidth()
            )

            //Llamada del boton inferior
            BotonSiguienteCodigo(
                onBotonSiguienteCodigo = {
                    onBotonSiguienteCodigo()
                }
            )

            Text(
                text = "No recibí el codigo",
                modifier = Modifier.padding(start = 220.dp, top = 15.dp)
            )

            Spacer(modifier = Modifier.height(220.dp))

            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun BotonSiguienteCodigo(

    onBotonSiguienteCodigo:() -> Unit

){
    Button(onClick = {
        onBotonSiguienteCodigo()
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